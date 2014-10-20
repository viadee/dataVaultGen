package de.viadee.dv.service.supplement;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Schema;
import de.viadee.dv.service.DimensionBuilder;

public class HierLinkTest extends AbstractIntegrationTest {

    @Autowired
    DimensionBuilder dimensionBuilder;

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvMediumHAL");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "1");
        System.setProperty("dwh.history", "true");
    }

    @Before
    public void executeGeneratedDDLStatements() {
        dropViewsAndTables();
        Schema schema = schemaExtractor.getSchema();
        dimensionBuilder.executeDimensionDDL(schema);
    }

    @Test
    public void testGeneratedViewDimKto() {
        int rowcount = jdbcTemplate.queryForObject("SELECT count(*) FROM " + targetSchemaName
                + ".DIM_GEO WHERE PARENT_SQN < SQN", Integer.class);
        assertTrue("Table DIM_GEO is missing or incomplete!", rowcount > 0);
    }

}
