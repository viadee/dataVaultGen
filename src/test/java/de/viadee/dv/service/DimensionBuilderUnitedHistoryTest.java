package de.viadee.dv.service;

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

/**
 * Tests the creation of united {@link Dimension}s (one Dimension for all {@link Satellite} of a {@link Hub}) <i>with
 * history</i>
 * 
 * @author B01
 *
 */
public class DimensionBuilderUnitedHistoryTest extends AbstractIntegrationTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "1");
        System.setProperty("dwh.history", "true");
    }

    @Autowired
    DimensionBuilder dimensionBuilder;

    @Before
    public void executeGeneratedDDLStatements() {
        dropViewsAndTables();
        Schema schema = schemaExtractor.getSchema();
        dimensionBuilder.executeDimensionDDL(schema);
    }

    @Test
    public void testGeneratedViewDimKtoWithHistoryField() {
        int rowcount = jdbcTemplate.queryForObject("SELECT count(VALID_FROM) FROM " + targetSchemaName + ".DIM_KTO",
                Integer.class);
        assertTrue("Table DIM_KTO is missing", rowcount > 0);
    }

    @Test
    public void testGeneratedViewDimUmsArtWithHistoryField() {
        int rowcount = jdbcTemplate.queryForObject(
                "SELECT count(VALID_FROM) FROM " + targetSchemaName + ".DIM_UMS_ART", Integer.class);
        assertTrue("Table DIM_UMS_ART is missing", rowcount > 0);
    }

    @Test
    public void testGeneratedViewDimKndWithHistoryField() {
        int rowcount = jdbcTemplate.queryForObject("SELECT count(VALID_FROM) FROM " + targetSchemaName + ".DIM_KND",
                Integer.class);
        assertTrue("Table DIM_KND is missing", rowcount > 0);
    }

}
