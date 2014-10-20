package de.viadee.dv.service.supplement.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Fact;
import de.viadee.dv.service.supplement.Materializer;
import de.viadee.dv.sql.SupplementDDLCompositor;

public class MaterializerImpl implements Materializer {

    private static final Logger LOGGER = LogManager.getLogger(MaterializerImpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SupplementDDLCompositor ddlCompositor;

    @Override
    public void materializeDimension(Dimension dimension, String history, String enhanceViews, String flatHierarchy) {
        String postfix = "";

        // materialize enhanced views
        if (history.equals("true") && (enhanceViews.equals("true"))) {
            postfix = "_ENH";
        } // materialize flat hierarchical viees
        else if (dimension.isHierarchical() && flatHierarchy.equals("true") && history.equals("false")) {
            postfix = "_FLAT";
        }

        String dimName = dimension.getDimensionName() + postfix;
        if (checkOrgAndNewTable(dimName)) {
            jdbcTemplate.execute(ddlCompositor.ddlForViewMaterializationAsSelect(dimName));
            LOGGER.info("Materialized View " + dimName + " as table " + dimName + "_MAT!");
        } else {
            LOGGER.info("View " + dimName + " is missing or table " + dimName + "_MAT already exists");
        }
    }

    @Override
    public void materializeFact(Fact fact, String history, String enhanceViews, String modus) {
        String postfix = "";
        if (history.equals("true") && enhanceViews.equals("true") && modus.equals("1")) {
            postfix = "_ENH";
        }
        String factName = fact.getFactName() + postfix;
        if (checkOrgAndNewTable(factName)) {
            jdbcTemplate.execute(ddlCompositor.ddlForViewMaterializationAsSelect(factName));
            LOGGER.info("Materialized View " + factName + " as table " + factName + "_MAT!");
        } else {
            LOGGER.info("View " + factName + " is missing or table " + factName + "_MAT already exists");
        }
    }

    private boolean checkOrgAndNewTable(String tableName) {
        boolean check = false;
        int checkOrgTable = jdbcTemplate
                .queryForObject(ddlCompositor.dmlToCheckIfTableExists(tableName), Integer.class);
        int checkNewTable = jdbcTemplate.queryForObject(ddlCompositor.dmlToCheckIfTableExists(tableName + "_MAT"),
                Integer.class);
        if (checkOrgTable > 0 && checkNewTable == 0) {
            check = true;
        }
        return check;
    }
}
