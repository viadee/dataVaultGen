package de.viadee.dv.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.repository.DimensionDAO;
import de.viadee.dv.sql.TargetDDLCompositor;

/**
 * Implementation of {@link DimensionDAO}.
 * 
 * @author B01
 *
 */
public class DimensionDAOImpl implements DimensionDAO {

    private String historyOn;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TargetDDLCompositor ddlCompositor;

    private static final Logger LOGGER = LogManager.getLogger(DimensionDAOImpl.class.getName());

    @Override
    public List<Dimension> createIndependentDimensions(Schema schema) {
        List<Dimension> dimensions = new ArrayList<Dimension>();
        for (Hub hub : schema.getHubs()) {
            for (Satellite sat : hub.getSatellites()) {
                Dimension dim = buildIndependentDimension(sat, hub);
                if (dim != null) {
                    dimensions.add(dim);
                }
            }
        }
        return dimensions;
    }

    @Override
    public List<Dimension> createUnitedDimensions(Schema schema) {
        List<Dimension> dimensions = new ArrayList<Dimension>();
        for (Hub hub : schema.getHubs()) {
            List<Satellite> satellites = hub.getSatellites();

            if (hub.isEquippedWithPitTable()) {
                Dimension dim = buildUnitedDimension(satellites, hub);
                if (dim != null) {
                    dimensions.add(dim);
                }
            } else {
                for (int i = 0; i < satellites.size(); i++) {
                    Dimension dim = buildIndependentDimension(satellites.get(i), hub);
                    if (dim != null) {
                        dimensions.add(dim);
                    }
                }
            }
        }
        return dimensions;
    }

    @Override
    public void executeDDLForDimension(Dimension dimension) {
        jdbcTemplate.execute(dimension.getDdlStatement());
        LOGGER.info("CREATED VIEW: " + dimension.getDimensionName());

    }

    /**
     * Builds independent {@link Dimension}s. For more detailed explanation please take a look at {@link DimensionDAO}.
     * 
     * @param sat
     * @param hub
     * @return {@link Dimension}
     */
    private Dimension buildIndependentDimension(Satellite sat, Hub hub) {
        List<String> fields = getDimensionFieldsFromSat(sat);

        if (fields.size() > 0) {
            Dimension dim = new Dimension();
            dim.setDimensionName(sat.getTablename().replace("SAT_", "DIM_"));
            dim.setFields(fields);
            dim.setBusinessKeyFieldName(hub.getBusinessKeyField());
            dim.setHierarchical(hub.hasHierLink());
            if (historyOn.equals("true")) {
                dim.setDdlStatement(ddlCompositor.ddlForindependentDimension(dim, sat.getTablename(),
                        hub.getTablename(), hub.hasHierLink()));
            } else {
                dim.setDdlStatement(ddlCompositor.ddlForIndependentDimensionNoHistory(dim, sat.getTablename(),
                        hub.getTablename(), hub.hasHierLink()));
            }
            LOGGER.debug("DDL for independent Dimension " + dim.getDimensionName() + " is: " + dim.getDdlStatement());
            return dim;
        } else {
            LOGGER.info("No Dimension for " + sat.getTablename());
            return null;
        }

    }

    /**
     * Builds united Dimensions. For more explanation please take a look at {@link DimensionDAO}.
     * 
     * @param satellites
     * @param hub
     * @return {@link Dimension}
     */
    private Dimension buildUnitedDimension(List<Satellite> satellites, Hub hub) {
        List<String> fields = new ArrayList<String>();
        List<String> satNames = new ArrayList<String>();
        for (Satellite sat : satellites) {
            if (!sat.isFactSatellite()) {
                for (String field : getDimensionFieldsFromSat(sat)) {
                    fields.add(field);
                }
                satNames.add(sat.getTablename());
            }
        }

        if (fields.size() > 0) {
            Dimension dim = new Dimension();
            dim.setDimensionName(hub.getTablename().replace("HUB_", "DIM_"));
            dim.setFields(fields);
            dim.setBusinessKeyFieldName(hub.getBusinessKeyField());
            dim.setHierarchical(hub.hasHierLink());
            if (historyOn.equals("true")) {
                dim.setDdlStatement(ddlCompositor.ddlForUnitedDimensions(dim, satNames, hub.getTablename(),
                        hub.hasHierLink()));
            } else {
                dim.setDdlStatement(ddlCompositor.ddlForUnitedDimensionsNoHistory(dim, satNames, hub.getTablename(),
                        hub.hasHierLink()));
            }
            LOGGER.debug("DDL for united Dimension " + dim.getDimensionName() + " is: " + dim.getDdlStatement());
            return dim;
        }
        return null;

    }

    /**
     * Depending on premises made on naming conventions, fields of future dimensions are determined using prefixes. This
     * list does not include any standard fields or fact fields like SQN, LOAD_DATE or LOAD_END_DATE
     * 
     * @param Satellite
     * @return {@link List} of Fields
     */
    private List<String> getDimensionFieldsFromSat(Satellite sat) {
        List<String> fields = new ArrayList<String>();
        for (String fieldInSat : sat.getFields()) {
            if (!fieldInSat.startsWith("SQN") && !fieldInSat.startsWith("LOAD_")
                    && !fieldInSat.startsWith("REC_SOURCE") && !fieldInSat.startsWith("F_")) {
                fields.add(sat.getTablename() + "." + fieldInSat);
            }
        }
        return fields;
    }

    public void setHistoryOn(String historyOn) {
        this.historyOn = historyOn;
    }

}
