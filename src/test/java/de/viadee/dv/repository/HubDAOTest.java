package de.viadee.dv.repository;

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
import de.viadee.dv.model.Link;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class HubDAOTest { // "Spring - Anschluss"

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
    }

    @Autowired
    HubDAO hubDAO;

    @Test
    public void testLoadHubs() {

        // If
        List<Hub> hubs = hubDAO.getAllHubsFromSchema();

        // Then
        assertEquals("Not all Hubs have been found!", 3, hubs.size());
    }

    @Test
    public void testFieldsOfGivenHub() {
        Hub hub = new Hub();
        hub.setTablename("HUB_KTO");

        List<String> fields = hubDAO.getFieldsFromHub(hub);
        assertEquals("Not all fields have been found!", 4, fields.size());
    }

    @Test
    public void testRelatedHubNamesFromLink() {
        Link link = new Link();
        link.setTablename("LINK_KTO_KND");

        List<String> tableNames = hubDAO.getRelatedHubNamesFromLink(link);
        assertTrue("Not all related Hubs have been found!",
                (tableNames.contains("HUB_KTO") && tableNames.contains("HUB_KND")));
    }

    /**
     * Not needed yet, because those checks do not enforce write operations
     */
    // @After
    // public void wipe() {
    // // ... Alle Views löschen
    // }

}
