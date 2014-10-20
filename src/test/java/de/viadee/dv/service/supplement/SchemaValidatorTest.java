package de.viadee.dv.service.supplement;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.viadee.dv.model.Schema;
import de.viadee.dv.service.SchemaExtractor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SchemaValidatorTest {

    @BeforeClass
    public static void setSchema() {
        System.setProperty("jdbc.schema", "dvTestError");
        System.setProperty("jdbc.targetschema", "dvTarget");
    }

    @Autowired
    SchemaExtractor schemaExtractor;

    @Test
    public void testSchemaValidator() {
        Schema schema = schemaExtractor.getSchema();
        assertTrue("The schema is valid but should be invalid for test.", !schema.isSchemaIsValid());

    }
}
