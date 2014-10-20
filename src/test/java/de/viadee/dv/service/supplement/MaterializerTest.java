package de.viadee.dv.service.supplement;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Schema;
import de.viadee.dv.service.DimensionBuilder;
import de.viadee.dv.service.FactBuilder;

public class MaterializerTest extends AbstractIntegrationTest {

    @Autowired
    DimensionBuilder dimensionBuilder;

    @Autowired
    FactBuilder factBuilder;

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvMedium");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "1");
        System.setProperty("dwh.history", "true");
        System.setProperty("dwh.flathierarchies", "true");
        System.setProperty("dwh.persist", "true");
    }

    @Before
    public void executeGeneratedDDLStatements() {
        dropViewsAndTables();
        Schema schema = schemaExtractor.getSchema();
        dimensionBuilder.executeDimensionDDL(schema);
        factBuilder.executeFactDDL(schema);
    }

    @Test
    public void testIfForeignKeyRelationsAreCorrect() {
        int rowcount = jdbcTemplate
                .queryForObject(
                        "select count(*) from information_schema.TABLES where TABLE_SCHEMA = 'dvTarget' and TABLE_NAME LIKE '%_MAT'",
                        Integer.class);
        assertTrue("The hashed foreign keys seem to be invalid.", rowcount == 6);
    }
}
