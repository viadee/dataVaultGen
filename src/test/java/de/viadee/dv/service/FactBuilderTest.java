package de.viadee.dv.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Schema;

public class FactBuilderTest extends AbstractIntegrationTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.enhance", "true");
        System.setProperty("dwh.history", "true");
    }

    @Autowired
    private FactBuilder factBuilder;

    @Autowired
    private DimensionBuilder dimDAO;

    @Before
    public void deleteFactViews() {
        dropViewsAndTables();
        Schema schema = schemaExtractor.getSchema();
        dimDAO.executeDimensionDDL(schema);
        factBuilder.executeFactDDL(schema);
    }

    @Test
    public void testGeneratedFactTables() {

        String sql = "SELECT count(*) FROM " + targetSchemaName + ".FACT_UMS";
        int rowcount = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals("Not all entries for FACT_UMS have been found!", 7, rowcount);

    }

    @Test
    public void testGeneratedFactFromSat() {
        String sql = "SELECT count(*) FROM " + targetSchemaName + ".FACT_KONTOSTAND_BEWERTUNGSZAHL";
        int rowcount = jdbcTemplate.queryForObject(sql, Integer.class);
        assertEquals("Not all entries for FACT_KONTOSTAND_BEWERTUNGSZAHL have been found!", 7, rowcount);

    }

    @Test
    public void testIfEnhancedFactTablesExist() {
        String ddl = "select count(*) from information_schema.TABLES where TABLE_SCHEMA='dvTarget' and TABLE_NAME like 'FACT_%_ENH' ;";
        int checkSum = jdbcTemplate.queryForObject(ddl, Integer.class);
        assertEquals("Not all enhanced tables have been found!", 2, checkSum);
    }
}
