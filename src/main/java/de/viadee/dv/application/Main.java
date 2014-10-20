package de.viadee.dv.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Build context to run application
 * 
 * @author B01
 *
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(final String[] args) {
        if (args.length > 0) {
            System.getProperties().setProperty("jdbc.schema", args[0]);
            LOGGER.info("Current schema: " + args[0]);
            // Target schema will be another than the source schema
            if (args.length > 1) {
                // PIT-Mode?
                if (args[1].equals("-pit")) {
                    System.getProperties().setProperty("source.pitmode", "on");
                    LOGGER.info("PIT-Mode activated. PIT-Tables will be generated in source database");
                } else {
                    System.getProperties().setProperty("jdbc.targetschema", args[1]);
                    LOGGER.info("Target schema: " + args[1]);
                }
            }
            // Target schema will be the same as the source schema
            else {
                System.getProperties().setProperty("jdbc.targetschema", args[0]);
                LOGGER.info("Target schema: " + args[0]);
            }
            final AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            context.close();
        } else {
            LOGGER.info("Please set a schema.");
        }
    }
}
