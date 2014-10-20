package de.viadee.dv.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
 * Tests {@link DimensionDAO} with dwh.modus = 1 (One united {@link Dimension} as result of {@link Hub}s with multiple
 * {@link Satellite}s)
 * 
 * @author B01
 *
 */
public class DimensionDAOUnitedTest extends AbstractIntegrationTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "1");
        System.setProperty("dwh.history", "true");
    }

    @Autowired
    DimensionDAO dimBuilder;

    @Before
    public void executeSchemaDrop() {
        dropViewsAndTables();
    }

    @Test
    public void testAmountOfIndependentDimensions() {
        Schema schema = schemaExtractor.getSchema();
        List<Dimension> dimensions = dimBuilder.createUnitedDimensions(schema);
        assertEquals("Not all united Dimensions will be generated!", 3, dimensions.size());
    }
}
