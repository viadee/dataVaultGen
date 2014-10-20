package de.viadee.dv.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Link;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.model.TransactionalLink;
import de.viadee.dv.repository.HubDAO;
import de.viadee.dv.repository.LinkDAO;
import de.viadee.dv.repository.SatelliteDAO;
import de.viadee.dv.service.SchemaExtractor;
import de.viadee.dv.service.pit.PITCreator;
import de.viadee.dv.service.supplement.SchemaValidator;

/**
 * SchemaExtractorImpl interacts with the database via <code>org.springframework.jdbc.core.JdbcTemplate</code> <br>
 * 
 * @author B01
 */
public class SchemaExtractorImpl implements SchemaExtractor {

    @Autowired
    private HubDAO hubDAO;

    @Autowired
    private LinkDAO linkDAO;

    @Autowired
    private SatelliteDAO satDAO;

    @Autowired
    private PITCreator pitCreator;

    @Autowired
    private SchemaValidator schemaValidtor;

    public Schema getSchema() {
        Schema schema = new Schema();
        schema.setHubs(hubDAO.getAllHubsFromSchema());
        schema.setLinks(linkDAO.getAllLinksFromSchema());
        schema.setTransactionalLinks(linkDAO.getAllTransActLinksFromSchema());

        associateHubsAndLinks(schema);
        associateHubsAndTransActLinks(schema);
        associateTransActLinksAndSatellites(schema);
        associateHubsAndSatellites(schema);

        schema.setSchemaIsValid(schemaValidtor.validateSchema(schema));

        return schema;
    }

    /**
     * Associates all {@link Hub} of a given schema with their related {@link Satellite}
     * 
     * @param schema
     */
    private void associateHubsAndSatellites(Schema schema) {
        for (Hub hub : schema.getHubs()) {
            List<Satellite> satellites = satDAO.getSatellitesForHub(hub);
            hub.setSatellites(satellites);
        }
    }

    /**
     * Associates all {@link Hub} of a given schema with their related {@link Link}
     * 
     * @param schema
     */
    private void associateHubsAndLinks(Schema schema) {
        for (Link link : schema.getLinks()) {
            List<String> hubNames = hubDAO.getRelatedHubNamesFromLink(link);
            List<Hub> referencedHubs = new ArrayList<Hub>();
            for (Hub hub : schema.getHubs()) {
                for (String name : hubNames) {
                    if (name.equals(hub.getTablename())) {
                        referencedHubs.add(hub);
                    }
                }
            }
            link.setReferencedHubs(referencedHubs);
        }

    }

    /**
     * Associates all {@link Hub} of a given schema with their related {@link TransactionalLink}
     * 
     * @param schema
     */
    private void associateHubsAndTransActLinks(Schema schema) {
        for (TransactionalLink taLink : schema.getTransactionalLinks()) {
            List<String> hubNames = hubDAO.getRelatedHubNamesFromLink(taLink);
            List<Hub> referencedHubs = new ArrayList<Hub>();
            for (Hub hub : schema.getHubs()) {
                for (String name : hubNames) {
                    if (name.equals(hub.getTablename())) {
                        referencedHubs.add(hub);
                    }
                }
            }
            taLink.setReferencedHubs(referencedHubs);
        }
    }

    /**
     * Associates all {@link TransactionalLink} of a given schema with their related {@link Satellite}
     */
    private void associateTransActLinksAndSatellites(Schema schema) {
        for (TransactionalLink link : schema.getTransactionalLinks()) {
            link.setReferencedSatellite(satDAO.getSatelliteForTransactionalLink(link));
        }
    }
}
