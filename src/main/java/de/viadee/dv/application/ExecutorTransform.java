package de.viadee.dv.application;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.viadee.dv.model.Schema;
import de.viadee.dv.service.DimensionBuilder;
import de.viadee.dv.service.FactBuilder;
import de.viadee.dv.service.SchemaExtractor;

/**
 * Class to run the application since no web service is provided. Will be executed automatically, after the dependency
 * injection is done.
 * 
 * @author B01
 */
@Component
public class ExecutorTransform {

    @Autowired
    @Qualifier(value = "pitMode")
    private String pitMode;

    @Autowired
    private SchemaExtractor schemaExtractor;

    @Autowired
    private DimensionBuilder dimenesionBuilder;

    @Autowired
    private FactBuilder factBuilder;

    private static final Logger LOGGER = LogManager.getLogger(ExecutorTransform.class.getName());

    @PostConstruct
    @Transactional
    public void execute() {

        if (pitMode.equals("off")) {

            Schema schema = schemaExtractor.getSchema();

            if (!schema.isSchemaIsValid()) {

                LOGGER.info("Schema is not valid. No Dimensions or Facts will be generated!");

            } else {

                dimenesionBuilder.executeDimensionDDL(schema);

                factBuilder.executeFactDDL(schema);
            }

        }

    }
}
