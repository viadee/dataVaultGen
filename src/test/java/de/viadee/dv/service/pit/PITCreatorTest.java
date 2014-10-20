package de.viadee.dv.service.pit;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Schema;

public class PITCreatorTest extends AbstractIntegrationTest {

    @Autowired
    private PITCreator pitCreator;

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvMedium");
        System.setProperty("source.pitmode", "on");
    }

    @Before
    public void executeGeneratedDDLStatements() {
        dropPitTables();
        Schema schema = schemaExtractor.getSchema();
        pitCreator.createPITTables(schema);
    }

    private void dropPitTables() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS dvMedium.PIT_HUB_KND");

    }

    @Test
    public void testGeneratedViewDimKto() {
        int rowcount = jdbcTemplate
                .queryForObject(
                        "select count(*) from information_schema.tables where TABLE_SCHEMA = 'dvMedium' and TABLE_NAME = 'PIT_HUB_KND'",
                        Integer.class);
        assertTrue("Table PIT_HUB_KND is missing", rowcount > 0);
    }
}
