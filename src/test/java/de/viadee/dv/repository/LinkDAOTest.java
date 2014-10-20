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

import de.viadee.dv.model.Link;
import de.viadee.dv.model.TransactionalLink;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class LinkDAOTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvLite");
        System.setProperty("jdbc.targetschema", "dvTarget");
    }

    @Autowired
    LinkDAO linkDAO;

    @Test
    public void testIfAllLinksOfSchemaExist() {
        List<Link> links = linkDAO.getAllLinksFromSchema();
        assertEquals("Not all links of schema have been found", links.size(), 1);
    }

    @Test
    public void testIfAllTransActLinksOfSchemaExist() {
        List<TransactionalLink> taLinks = linkDAO.getAllTransActLinksFromSchema();
        assertEquals("Not all transactional links habe been found", 1, taLinks.size());
    }
}
