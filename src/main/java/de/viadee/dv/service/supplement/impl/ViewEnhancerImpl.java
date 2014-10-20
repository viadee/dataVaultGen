package de.viadee.dv.service.supplement.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.service.supplement.ViewEnhancer;
import de.viadee.dv.sql.SupplementDDLCompositor;

public class ViewEnhancerImpl implements ViewEnhancer {

    private static final Logger LOGGER = LogManager.getLogger(ViewEnhancerImpl.class.getName());

    @Autowired
    private SupplementDDLCompositor ddlCompositor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String history;

    @Override
    public void enhanceViewsForDimensions(Dimension dim) {
        String ddl = "";
        if (history.equals("true")) { // Enhancement of Dimensions with history means adding surrogate keys
            ddl = ddlCompositor.ddlForDimViewEnhancement(dim);
            LOGGER.debug("DDL for enhancement of dimension " + dim.getDdlStatement() + ": " + ddl);
            jdbcTemplate.execute(ddl);
            LOGGER.info("CREATED ENHANCED VIEW OF: " + dim.getDimensionName() + "_ENH");
        }
    }

    @Override
    public void enhanceViewsForFactsFromTaLinks(FactFromTaLink fact) {
        if (history.equals("true")) {
            String ddl = ddlCompositor.ddlForFactFromTaLinkViewEnhancement(fact);
            LOGGER.debug("DDL for enhancement of Facts from TA_LINKs " + fact.getOriginTaLinkName() + ": " + ddl);
            jdbcTemplate.execute(ddl);
            LOGGER.info("CREATED ENHANCED VIEW: " + fact.getFactName() + "_ENH");
        }
    }

    @Override
    public void enhanceViewsForFactsFromSatellites(FactFromSat fact) {
        if (history.equals("true")) {
            String dimName = fact.getAssociatedHubName().replace("HUB_", "DIM_");
            if (getCheckSumForViewOrTable(dimName) > 0) {
                String ddlString = ddlCompositor.ddlForFactFromSatViewEnhancement(fact, dimName);
                LOGGER.debug("DDL for enhancement of Facts from Satellite " + fact.getOriginSatelliteName() + ": "
                        + ddlString);
                jdbcTemplate.execute(ddlString);
                LOGGER.info("CREATED ENHANCED VIEW: " + fact.getFactName() + "_ENH");
            } else {
                LOGGER.info("Dimension " + dimName + " is missing. View of fact " + fact.getFactName()
                        + " has not been enhanced. Please check naming convention in source DataVault!");
            }
        }
    }

    /**
     * Queries the database to check whether the materialized view for a given view already exists
     * 
     * @param viewName
     * @return checkSum
     */
    private int getCheckSumForViewOrTable(String viewName) {
        String checkDml = ddlCompositor.dmlToCheckIfTableExists(viewName);
        return jdbcTemplate.queryForObject(checkDml, Integer.class);
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
