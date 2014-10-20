package de.viadee.dv.application;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.viadee.dv.model.Schema;
import de.viadee.dv.service.SchemaExtractor;
import de.viadee.dv.service.pit.PITCreator;

@Component
public class ExecutorPIT {

    @Autowired
    @Qualifier(value = "pitMode")
    private String pitMode;

    @Autowired
    private SchemaExtractor schemaExtractor;

    @Autowired
    private PITCreator pitCreator;

    private static final Logger LOGGER = LogManager.getLogger(ExecutorPIT.class.getName());

    @PostConstruct
    @Transactional
    public void execute() {

        if (pitMode.equals("on")) {
            Schema schema = schemaExtractor.getSchema();

            if (!schema.isSchemaIsValid()) {

                LOGGER.info("Schema is not valid. No PIT-Tables will be generated!");

            } else {
                pitCreator.createPITTables(schema);
            }
        }

    }
}
