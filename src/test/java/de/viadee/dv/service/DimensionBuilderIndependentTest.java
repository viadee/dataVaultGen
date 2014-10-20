package de.viadee.dv.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Schema;

/**
 * Tests the creation of independent dimensions (one {@link Dimension} for each {@link Hub}) <i>without history</i>
 * 
 * @author B01
 *
 */
public class DimensionBuilderIndependentTest extends AbstractIntegrationTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "0");
        System.setProperty("dwh.history", "false");
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
    public void testGeneratedViewDimKto() {
        int rowcount = jdbcTemplate.queryForObject("SELECT count(*) FROM " + targetSchemaName + ".DIM_KTO",
                Integer.class);
        assertTrue("Table DIM_KTO is missing", rowcount > 0);
        System.out.println(targetSchemaName);
    }

    @Test
    public void testGeneratedViewDimKndAddress() {
        int rowcount = jdbcTemplate.queryForObject("SELECT count(*) FROM " + targetSchemaName + ".DIM_KND_ADDRESS",
                Integer.class);
        assertTrue("Table DIM_KND_ADDRESS is missing", rowcount > 0);
    }

    @Test
    public void testGeneratedViewDimKnd() {
        int rowcount = jdbcTemplate.queryForObject("SELECT count(*) FROM " + targetSchemaName + ".DIM_KND",
                Integer.class);
        assertTrue("Table DIM_KND is missing", rowcount > 0);
    }

    @Test
    public void testGeneratedViewDimUmsArt() {
        int rowcount = jdbcTemplate.queryForObject("SELECT count(*) FROM " + targetSchemaName + ".DIM_UMS_ART",
                Integer.class);
        assertTrue("Table DIM_UMS_ART is missing", rowcount > 0);
    }

}
