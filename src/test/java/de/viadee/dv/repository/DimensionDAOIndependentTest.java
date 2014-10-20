package de.viadee.dv.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Schema;

/**
 * Tests {@link DimensionDAO} with dwh.modus = 0 (Independent dimensions as result)
 * 
 * @author B01
 *
 */
public class DimensionDAOIndependentTest extends AbstractIntegrationTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "0");
        System.setProperty("dwh.history", "true");
    }

    @Autowired
    DimensionDAO dimensionDAO;

    @Test
    public void testAmountOfIndependentDimensions() {
        dropViewsAndTables();
        Schema schema = schemaExtractor.getSchema();
        List<Dimension> dimensions = dimensionDAO.createIndependentDimensions(schema);
        assertEquals("Not all Dimensions will be generated!", 4, dimensions.size());
    }

}
