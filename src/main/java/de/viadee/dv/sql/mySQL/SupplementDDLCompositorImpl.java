package de.viadee.dv.sql.mySQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.model.Hierarchy;
import de.viadee.dv.sql.SupplementDDLCompositor;

public class SupplementDDLCompositorImpl implements SupplementDDLCompositor {

    @Autowired
    @Qualifier(value = "targetSchemaName")
    private String targetSchemaName;

    @Override
    public String ddlForDimViewEnhancement(Dimension dim) {
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + dim.getDimensionName() + "_ENH AS SELECT ";
        ddl = ddl + "md5(concat_ws('_',a.SQN, a.VALID_FROM)) as " + dim.getDimensionName() + "_ID,";
        ddl = ddl + " a.* from " + targetSchemaName + "." + dim.getDimensionName() + " a";
        return ddl;
    }

    @Override
    public String ddlForFactFromTaLinkViewEnhancement(FactFromTaLink fact) {
        String ddlCreate = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + fact.getFactName() + "_ENH AS ";
        String ddlSelect = "SELECT " + fact.getFactName() + ".SQN ";
        String ddlFrom = "";
        String ddlWhere = "";
        for (String field : fact.getFieldsWithOrigin()) {
            if (field.contains("_SQN")) {

                String dimName = field.split("\\.")[1].replace("HUB_", "DIM_").replace("_SQN", "");
                String dimNameField = field.split("\\.")[1];

                // concat unique key from SQN and VALID_FROM-Fields
                ddlSelect = ddlSelect + ", md5(concat_ws('_'," + dimName + ".SQN, " + dimName + ".VALID_FROM)) as "
                        + dimName + "_ID";

                if (ddlFrom.length() == 0) {
                    ddlFrom = ddlFrom + " FROM " + targetSchemaName + "." + fact.getFactName();
                }
                ddlFrom = ddlFrom + " JOIN " + targetSchemaName + "." + dimName + " ON " + targetSchemaName + "."
                        + fact.getFactName() + "." + dimNameField + " = " + dimName + ".SQN";

                // Concat where-clause for fact including history
                if (ddlWhere.length() == 0) {
                    ddlWhere = ddlWhere + " WHERE ";
                } else {
                    ddlWhere = ddlWhere + " AND ";
                }

                ddlWhere = ddlWhere + "((" + fact.getFactName() + ".LOAD_DATE between " + dimName + ".VALID_FROM and "
                        + dimName + ".VALID_TO) OR (" + fact.getFactName() + ".LOAD_DATE >= " + dimName
                        + ".VALID_FROM and " + dimName + ".VALID_TO is NULL))";

            } else if (!field.split("\\.")[1].equals("SQN")) {
                ddlSelect = ddlSelect + ", " + fact.getFactName() + "." + field.split("\\.")[1];
            }
        }
        return ddlCreate + ddlSelect + ddlFrom + ddlWhere;
    }

    @Override
    public String ddlForFactFromSatViewEnhancement(FactFromSat fact, String dimName) {
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + fact.getFactName() + "_ENH AS SELECT "
                + fact.getFactName() + ".SQN";
        ddl = ddl + ", md5(concat_ws('_'," + dimName + ".SQN, " + dimName + ".VALID_FROM)) as " + dimName + "_ID";

        for (String field : fact.getFieldsWithOrigin()) {
            if (!field.contains("SQN")) {
                ddl = ddl + ", " + fact.getFactName() + "." + field.split("\\.")[1];
            }
        }

        ddl = ddl + " FROM " + targetSchemaName + "." + fact.getFactName() + " JOIN " + targetSchemaName + "."
                + dimName + " ON " + fact.getFactName() + ".SQN = " + dimName + ".SQN";

        // Concat where-clause for facts including history
        ddl = ddl + " WHERE ((" + fact.getFactName() + ".LOAD_DATE BETWEEN " + dimName + ".VALID_FROM AND " + dimName
                + ".VALID_TO) OR (" + fact.getFactName() + ".LOAD_DATE" + ">=" + dimName + ".VALID_FROM and " + dimName
                + ".VALID_TO IS NULL))";
        return ddl;
    }

    @Override
    public String ddlForHierarchyFlattening(Dimension dim, Hierarchy hierarchy) {
        Integer maxDepth = hierarchy.getMaxHierDepth();
        String ddlCreate = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + dim.getDimensionName() + "_FLAT AS ";

        // Reads SQN and Business Key from entry of lowest level
        String ddlSelect = "SELECT t" + maxDepth + ".SQN, t" + maxDepth + dim.getDimensionName().replace("DIM_", ".")
                + "_NUMBER";
        String ddlFrom = "";
        String ddlWhere = "";

        for (int depth = maxDepth; depth > 0; depth--) {
            // Add fields from all levels to SQL-Statement to ensure that no information gets lost.
            for (String field : dim.getFields()) {
                String fieldWithoutSource = field.substring(field.indexOf(".") + 1);
                ddlSelect = ddlSelect + ", t" + depth + "." + fieldWithoutSource + " as " + fieldWithoutSource + "_"
                        + depth;
            }
            if (depth == maxDepth) {
                ddlFrom = ddlFrom + " FROM ";
            } else {
                ddlFrom = ddlFrom + " JOIN ";
                if (depth == maxDepth - 1) {
                    ddlWhere = ddlWhere + " WHERE ";
                } else {
                    ddlWhere = ddlWhere + " AND ";
                }

                ddlWhere = ddlWhere + "t" + (depth + 1) + ".PARENT_SQN  = t" + depth + ".SQN";
            }
            ddlFrom = ddlFrom + targetSchemaName + "." + dim.getDimensionName() + " t" + depth + " ";
        }
        return ddlCreate + ddlSelect + ddlFrom + ddlWhere;
    }

    @Override
    public String dmlToCheckIfTableExists(String viewOrTableName) {
        return "select count(*) from information_schema.TABLES WHERE TABLE_SCHEMA = '" + targetSchemaName
                + "' and TABLE_NAME = '" + viewOrTableName + "'";
    }

    @Override
    public String dmlForSelectHierFieldsFromHierarchyDimension(String dimensionName) {
        return "SELECT SQN, PARENT_SQN FROM " + targetSchemaName + "." + dimensionName;
    }

    @Override
    public String ddlForViewMaterializationAsSelect(String factOrDimName) {
        return "CREATE TABLE " + targetSchemaName + "." + factOrDimName + "_MAT AS SELECT * FROM " + targetSchemaName
                + "." + factOrDimName;
    }
}
