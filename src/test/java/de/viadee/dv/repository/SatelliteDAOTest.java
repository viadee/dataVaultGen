package de.viadee.dv.repository;

import static org.junit.Assert.assertEquals;

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
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.TransactionalLink;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SatelliteDAOTest { // "Spring - Anschluss"

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
    }

    @Autowired
    SatelliteDAO satDAO;

    @Test
    public void testGetSatellitesFromHub() {
        Hub hub = new Hub();
        hub.setTablename("HUB_KTO");

        List<Satellite> satellites = satDAO.getSatellitesForHub(hub);
        assertEquals("Not all Satellites have been found for Hub " + hub.getTablename(), satellites.size(), 2);

    }

    @Test
    public void testGetSatellitesFromTransActLink() {
        TransactionalLink link = new TransactionalLink();
        link.setTablename("TA_LINK_UMS");

        Satellite satellites = satDAO.getSatelliteForTransactionalLink(link);
        assertEquals("Not all Satellites have been found for Link " + link.getTablename(), satellites.getTablename(),
                "SAT_UMS");
    }

}
