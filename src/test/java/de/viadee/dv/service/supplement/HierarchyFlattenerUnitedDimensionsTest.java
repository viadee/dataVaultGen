package de.viadee.dv.service.supplement;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.service.DimensionBuilder;

/**
 * Tests if multiple {@link Satellite}s of a hierarchical {@link Hub} result into a single and corrent {@link Dimension}
 * 
 * @author B01
 *
 */
public class HierarchyFlattenerUnitedDimensionsTest extends AbstractIntegrationTest {

    @Autowired
    DimensionBuilder dimensionBuilder;

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvMediumHAL");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "1");
        System.setProperty("dwh.history", "false");
        System.setProperty("dwh.flathierarchies", "true");
        System.setProperty("dwh.persist", "true");
    }

    @Before
    public void executeGeneratedDDLStatements() {
        dropViewsAndTables();
        Schema schema = schemaExtractor.getSchema();
        dimensionBuilder.executeDimensionDDL(schema);
    }

    /**
     * PIT-Table must be existent to check, whether two {@link Satellite}s will be united into one flat hierarchical
     * {@link Dimension}
     */
    @Test
    public void testIfHierarchyHasPitTable() {
        int rowcount = jdbcTemplate
                .queryForObject(
                        "select count(*) from information_schema.TABLES where TABLE_SCHEMA='dvMediumHAL' and TABLE_NAME like 'PIT_%_GEO'",
                        Integer.class);
        assertTrue("PIT-Table for hierarchical Dimension is missing!", rowcount == 1);
    }

    /**
     * The two {@link Satellite}s of the {@link Hub}_GEO must be flattenend and united into 1 {@link Dimension}
     */
    @Test
    public void testIfHierarchyHasBeenFlattened() {
        int rowcount = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.TABLES where TABLE_SCHEMA='" + targetSchemaName
                        + "' and TABLE_NAME LIKE '%_FLAT_MAT';", Integer.class);
        assertTrue("A flat hierarchy is missing!", rowcount == 1);
    }
}
