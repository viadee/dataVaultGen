package de.viadee.dv.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.AbstractIntegrationTest;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.model.Schema;

/**
 * Tests {@link FactDAO}
 * 
 * @author B01
 *
 */
public class FactDAOTest extends AbstractIntegrationTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
    }

    @Autowired
    FactDAO factDAO;

    @Test
    public void testFactsFromSatellites() {
        Schema schema = schemaExtractor.getSchema();
        List<FactFromSat> facts = factDAO.createFactsFromSatellites(schema);

        assertEquals("Not all facts originating from satellites have been found!", 1, facts.size());
    }

    @Test
    public void testFactsFromTransactionalLinks() {
        Schema schema = schemaExtractor.getSchema();
        List<FactFromTaLink> facts = factDAO.createFactsFromTaLinks(schema);

        assertEquals("Not all facts originating from satellites have been found!", 1, facts.size());
    }

}
