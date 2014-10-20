package de.viadee.dv.service.supplement.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Link;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.model.TransactionalLink;
import de.viadee.dv.service.supplement.SchemaValidator;

public class SchemaValidatorImpl implements SchemaValidator {

    private static final Logger LOGGER = LogManager.getLogger(SchemaValidatorImpl.class.getName());

    @Override
    public boolean validateSchema(Schema schema) {

        boolean linksAreValid = validateLinks(schema);
        boolean transactionalLinksAreValid = validateTransactionalLinks(schema);
        boolean hubsAreValid = validateHubs(schema);

        return linksAreValid && hubsAreValid && transactionalLinksAreValid;
    }

    private boolean validateTransactionalLinks(Schema schema) {
        boolean taLinkIsValid = true;

        for (TransactionalLink taLink : schema.getTransactionalLinks()) {
            boolean validFields = validateFields(taLink.getFields(), "TA_LINK_", taLink.getTablename());
            boolean validHubConnection = validateHubConnections(taLink.getReferencedHubs(), taLink.getTablename());
            boolean validSatellites = validateSatellite(taLink.getReferencedSatellite(), taLink.getTablename());
            if (!validFields || !validHubConnection || !validSatellites) {
                taLinkIsValid = false;
            }
        }
        return taLinkIsValid;
    }

    /**
     * Executes all hub validations
     * 
     * @param schema
     * @return true if hubs are all valid
     */
    private boolean validateHubs(Schema schema) {
        boolean hubsAreValid = true;
        for (Hub hub : schema.getHubs()) {
            boolean hubIsValid = validateHubFields(hub);
            boolean hubHasValidAmountOfSats = validateAmountOfSatellites(hub.getSatellites(), hub.getTablename());
            boolean hubSatellitesAreValid = true;
            for (Satellite sat : hub.getSatellites()) {
                if (!validateSatellite(sat, hub.getTablename())) {
                    hubSatellitesAreValid = false;
                }
            }

            if (!hubIsValid || !hubHasValidAmountOfSats || !hubSatellitesAreValid) {
                hubsAreValid = false;
            }
        }
        return hubsAreValid;
    }

    private boolean validateSatellite(Satellite satellite, String originTableName) {
        boolean satIsValid = true;
        if (satellite == null) {
            satIsValid = false;
            LOGGER.error("Satellite from " + originTableName + " does not exists");
        } else if (!validateSatelliteFields(satellite)) {
            satIsValid = false;
        }
        return satIsValid;
    }

    /**
     * Executes all {@link Link} validations.
     * 
     * @param schema
     * @return true if no violations have been detected.
     */
    private boolean validateLinks(Schema schema) {
        boolean linkIsValid = true;
        for (Link link : schema.getLinks()) {
            boolean validFields = validateFields(link.getFields(), "LINK_", link.getTablename());
            boolean validHubConnection = validateHubConnections(link.getReferencedHubs(), link.getTablename());
            if (!(validFields || validHubConnection)) {
                linkIsValid = false;
            }
        }
        return linkIsValid;
    }

    /**
     * Rule: A link has to union at least two hubs!
     * 
     * @param list
     * @param tableName
     * @return true if link unions at least two hubs
     */
    private boolean validateHubConnections(List<Hub> list, String tableName) {
        boolean linkHasMinAmountOfHubs = true;
        int amountOfHubs = list.size();
        if (amountOfHubs < 2) {
            LOGGER.error("Link " + tableName + " does not union at least two hubs. Link is invalid!");
            linkHasMinAmountOfHubs = false;
        } else {
            LOGGER.debug("Link " + tableName + " does union minimum amount of hubs (2)");
        }
        return linkHasMinAmountOfHubs;
    }

    /**
     * Rule: Links and Satellites need to be equipped with the fields SQN and LOAD_DATE
     * 
     * @param fields
     * @param objType
     * @return true, if no violations have been detected.
     */
    private boolean validateFields(List<String> fields, String objType, String tableName) {
        boolean objectHasAllNecessaryFields = true;

        if (!(fields.contains("SQN") && fields.contains("LOAD_DATE"))) {
            objectHasAllNecessaryFields = false;
            LOGGER.error(tableName + " does not contain all required fields. SQN oder LOAD_DATE are missing!");
        } else {
            LOGGER.debug(tableName + " does contain all required fields.");
        }

        return objectHasAllNecessaryFields;
    }

    /**
     * Rule: A Hub names HUB_GEO has to contain the fields GEO_NUMBER, GEO_LOAD_DATE and SQN.
     * 
     * @param hub
     * @return true, if hub contains all necessary fields
     */
    private boolean validateHubFields(Hub hub) {
        boolean hubContainsNecessaryFields = true;
        String shortName = hub.getTablename().replace("HUB_", "");

        if (hub.getFields().contains("SQN") && hub.getFields().contains(shortName + "_NUMBER")
                && hub.getFields().contains(shortName + "_LOAD_DATE")) {
            LOGGER.debug("Hub " + shortName + " contains all necessary fields!");
        } else {
            LOGGER.error("Hub " + shortName + " does not contain all necessary fields!");
            hubContainsNecessaryFields = false;
        }

        return hubContainsNecessaryFields;
    }

    /**
     * Rule: {@link Satellite}s need the fields SQN and LOAD_DATE
     * 
     * @param satellite
     * @return true if no violations have been detected
     */
    private boolean validateSatelliteFields(Satellite satellite) {
        boolean satHasAllRequiredFields = true;

        if (!(satellite.getFields().contains("SQN") && satellite.getFields().contains("LOAD_DATE"))) {
            satHasAllRequiredFields = false;
            LOGGER.error(satellite.getTablename() + " does not have all required fields!");
        } else {
            LOGGER.debug(satellite.getTablename() + " has all required fields!");
        }

        return satHasAllRequiredFields;
    }

    /**
     * Rule: {@link TransactionalLink}s and {@link Hub}s need at least one {@link Satellite}.
     * 
     * @param satellites
     * @param tableName
     * @return true, if no violations have been detected
     */
    private boolean validateAmountOfSatellites(List<Satellite> satellites, String tableName) {
        boolean objHasMinAmountOfSatellites = true;
        if (satellites == null) {
            LOGGER.error(tableName + " does not have any Satellites!");
        } else if (satellites.size() == 0) {
            objHasMinAmountOfSatellites = false;
            LOGGER.error(tableName + " does not have the minimum amount of satellites (1");
        } else {
            LOGGER.debug(tableName + " has the minimum amount of satellites (1)");
        }

        // for (Satellite sat : satellites) {
        // boolean validSatellites = validateSatellite(sat, tableName);
        // if (!validSatellites) {
        // objHasMinAmountOfSatellites = false;
        // }
        // }

        return objHasMinAmountOfSatellites;
    }
}
