package de.viadee.dv.etc;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Schema;
import de.viadee.dv.service.DimensionBuilder;
import de.viadee.dv.service.FactBuilder;

/**
 * Tests if application runs through correctly with a totally different data vault schema.
 * 
 * @author B01
 *
 */
public class DifferentSchemaTest extends AbstractIntegrationTest {

    @Autowired
    DimensionBuilder dimensionBuilder;

    @Autowired
    FactBuilder factBuilder;

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvTest");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "1");
        System.setProperty("dwh.history", "true");
        System.setProperty("dwh.persist", "true");
        System.setProperty("dwh.enhance", "true");
    }

    @Before
    public void executeGeneratedDDLStatements() {
        dropViewsAndTables();
        Schema schema = schemaExtractor.getSchema();
        dimensionBuilder.executeDimensionDDL(schema);
        factBuilder.executeFactDDL(schema);
    }

    @Test
    public void testIfTaLinkWith3HubsIsPopulatedCorrectly() {
        int rowcount = jdbcTemplate
                .queryForObject(
                        "select count(*) from dvTarget.FACT_SALE_ENH_MAT a join dvTarget.DIM_PRODUCT_ENH_MAT b on a.DIM_PRODUCT_ID = b.DIM_PRODUCT_ID join dvTarget.DIM_STORE_ENH_MAT c on a.DIM_STORE_ID = c.DIM_STORE_ID join dvTarget.DIM_CAT_ENH_MAT d on a.DIM_CAT_ID = d.DIM_CAT_ID",
                        Integer.class);
        assertTrue("A flat hierarchy is missing!", rowcount == 3);
    }
}
