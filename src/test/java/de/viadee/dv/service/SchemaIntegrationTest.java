package de.viadee.dv.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Schema;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SchemaIntegrationTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvMedium");
        System.setProperty("jdbc.targetschema", "dvTarget");
        System.setProperty("dwh.modus", "1");
    }

    @Autowired
    SchemaExtractor schemaExtractor;

    @Test
    public void testIfSchemaIsValid() {
        Schema schema = schemaExtractor.getSchema();
        assertTrue("Schema dvMedium is not valid!", schema.isSchemaIsValid());
    }

    @Test
    public void testHubsInSchema() {
        Schema schema = schemaExtractor.getSchema();
        List<Hub> hubs = schema.getHubs();

        assertEquals("Not all hubs in schema have been found!", 4, hubs.size());

    }

    @Test
    public void testSatellitesInSchema() {
        Schema schema = schemaExtractor.getSchema();
        int amountOfSatellitesViaHub = 0;

        for (Hub hub : schema.getHubs()) {
            amountOfSatellitesViaHub = amountOfSatellitesViaHub + hub.getSatellites().size();
        }

        assertEquals("Not all Satellites have been found!", 6, amountOfSatellitesViaHub);

    }
}
