package de.viadee.dv.service.supplement;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.service.DimensionBuilder;

/**
 * Tests if the {@link Satellite}s of a hub result in single hierarchical {@link Dimension}s
 * 
 * @author B01
 *
 */
public class HierarchyFlattenerTest extends AbstractIntegrationTest {

    @Autowired
    DimensionBuilder dimensionBuilder;

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvMediumHAL");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "0");
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

    @Test
    public void testIfHierarchyHasBeenFlattened() {
        int rowcount = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.TABLES where TABLE_SCHEMA='" + targetSchemaName
                        + "' and TABLE_NAME LIKE '%_FLAT_MAT';", Integer.class);
        assertTrue("A flat hierarchy is missing!", rowcount > 0);
    }
}
