package de.viadee.dv.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.viadee.dv.model.Fact;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.model.TransactionalLink;
import de.viadee.dv.repository.FactDAO;
import de.viadee.dv.sql.TargetDDLCompositor;

public class FactDAOImpl implements FactDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TargetDDLCompositor ddlCompositor;

    private static final Logger LOGGER = LogManager.getLogger(FactDAOImpl.class.getName());

    @Override
    public List<FactFromSat> createFactsFromSatellites(Schema schema) {
        List<FactFromSat> facts = new ArrayList<FactFromSat>();
        for (Hub hub : schema.getHubs()) {
            for (Satellite sat : hub.getSatellites()) {

                List<String> fieldsForFact = new ArrayList<String>();
                fieldsForFact.add(sat.getTablename() + ".SQN");
                fieldsForFact.add(sat.getTablename() + ".LOAD_DATE");
                String factname = "FACT";
                for (String field : sat.getFields()) {
                    if (field.startsWith("F_")) {
                        fieldsForFact.add(sat.getTablename() + "." + field);
                        factname = factname + field.replace("F_", "_");
                    }
                }

                FactFromSat fact = new FactFromSat();
                if (fieldsForFact.size() > 2) {
                    fact.setFields(fieldsForFact);
                    fact.setAssociatedHubName(hub.getTablename());
                    fact.setOriginSatelliteName(sat.getTablename());
                    fact.setFactName(factname);
                    fact.setDdlStatement(ddlCompositor.ddlForFactFromSat(fact));
                    facts.add(fact);
                    LOGGER.debug("DDL for Fact from Satellite " + sat.getTablename() + " is: " + fact.getDdlStatement());
                }
            }
        }
        return facts;
    }

    @Override
    public void executeDDLForFactFromSatelliteOrTaLink(Fact fact) {
        jdbcTemplate.execute(fact.getDdlStatement());
        LOGGER.info("CREATED VIEW: " + fact.getFactName());
    }

    @Override
    public List<FactFromTaLink> createFactsFromTaLinks(Schema schema) {
        List<FactFromTaLink> factsFromLink = new ArrayList<FactFromTaLink>();
        for (TransactionalLink taLink : schema.getTransactionalLinks()) {
            FactFromTaLink fact = new FactFromTaLink();
            fact.setOriginTaLinkName(taLink.getTablename());
            fact.setDescribingSatelliteName(taLink.getReferencedSatellite().getTablename());
            fact.setFields(determineFieldsForFactFromTaLink(taLink));
            fact.setFactName(taLink.getTablename().replace("TA_LINK_", "FACT_"));
            fact.setDdlStatement(ddlCompositor.ddlForFactsFromTaLink(fact));
            factsFromLink.add(fact);
            LOGGER.debug("DDL for Fact from TaLink " + taLink.getTablename() + " is: " + fact.getDdlStatement());
        }
        return factsFromLink;
    }

    /**
     * Looks up relevant fields from referenced from {@link TransactionalLink} and the describing {@link Satellite}
     * 
     * @param taLink
     * @return List of fields and the table, the originate from like [table].[field]
     */
    private List<String> determineFieldsForFactFromTaLink(TransactionalLink taLink) {
        List<String> fields = new ArrayList<String>();

        for (String linkField : taLink.getFields()) {
            if (linkField.contains("SQN")) {
                fields.add(taLink.getTablename() + "." + linkField);
            }
        }

        for (String field : taLink.getReferencedSatellite().getFields()) {
            if (!field.equals("SQN") && !field.equals("REC_SOURCE")) {
                fields.add(taLink.getReferencedSatellite().getTablename() + "." + field);
            }
        }

        return fields;
    }

}
