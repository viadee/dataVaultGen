package de.viadee.dv.sql.mySQL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.sql.TargetDDLCompositor;

public class TargetDDLCompositorImpl implements TargetDDLCompositor {

    @Autowired
    @Qualifier(value = "schemaName")
    private String schemaName;

    @Autowired
    @Qualifier(value = "targetSchemaName")
    private String targetSchemaName;

    @Override
    public String ddlForindependentDimension(Dimension dim, String satName, String hubName, Boolean hasHierLink) {
        // CREATE-Statement with dimensionsTableName
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + dim.getDimensionName() + " AS SELECT ";
        // Sequence Number and Business Key
        ddl = ddl + satName + ".SQN, " + hubName + "." + dim.getBusinessKeyFieldName() + ", ";

        // TODO activate the following if hierarchy flattening is provided with enabled history
        // Add parent if hub has hierarchical link
        // if (hasHierLink) {
        // ddl = ddl + hubName.replace("HUB_", "HAL_LINK_") + "." + hubName + "_PARENT_SQN as PARENT_SQN, ";
        // }

        // SCD Type II fields -> History
        ddl = ddl + satName + ".LOAD_DATE as VALID_FROM, " + satName + ".LOAD_END_DATE as VALID_TO, case when "
                + satName + ".LOAD_END_DATE IS NOT NULL then 0 else 1 end as IS_VALID,";

        List<String> s = dim.getFields();
        for (int i = 0; i < s.size(); i++) {
            if (i == s.size() - 1) {
                ddl = ddl + " " + s.get(i);
            } else {
                ddl = ddl + " " + s.get(i) + ",";
            }
        }

        // join of hub and satellite
        ddl = ddl + " FROM " + schemaName + "." + satName + " join " + schemaName + "." + hubName + " on " + satName
                + ".SQN = " + hubName + ".SQN";

        // TODO activate the following if hierarchy flattening is provided with enabled history
        // if (hasHierLink) {
        // ddl = ddl + " LEFT JOIN " + schemaName + "." + hubName.replace("HUB_", "HAL_LINK_") + " ON " + hubName
        // + ".SQN = " + hubName.replace("HUB_", "HAL_LINK_") + "." + hubName + "_SQN";
        // }
        return ddl;
    }

    @Override
    public String ddlForIndependentDimensionNoHistory(Dimension dim, String satName, String hubName, Boolean hasHierLink) {
        // CREATE-Statement with dimensionsTableName
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + dim.getDimensionName() + " AS SELECT ";
        // Sequence Number and Business Key
        ddl = ddl + satName + ".SQN, " + hubName + "." + dim.getBusinessKeyFieldName() + ", ";
        // Add parent if hub has hierarchical link
        if (hasHierLink) {
            ddl = ddl + hubName.replace("HUB_", "HAL_LINK_") + "." + hubName + "_PARENT_SQN as PARENT_SQN, ";
        }

        List<String> s = dim.getFields();
        for (int i = 0; i < s.size(); i++) {
            if (i == s.size() - 1) {
                ddl = ddl + " " + s.get(i);
            } else {
                ddl = ddl + " " + s.get(i) + ",";
            }
        }

        // join of hub and satellite
        ddl = ddl + " FROM " + schemaName + "." + satName + " join " + schemaName + "." + hubName + " on " + satName
                + ".SQN = " + hubName + ".SQN";

        if (hasHierLink) {
            ddl = ddl + " LEFT JOIN " + schemaName + "." + hubName.replace("HUB_", "HAL_LINK_") + " ON " + hubName
                    + ".SQN = " + hubName.replace("HUB_", "HAL_LINK_") + "." + hubName + "_SQN";
        }
        ddl = ddl + " WHERE " + satName + ".LOAD_END_DATE IS NULL";
        return ddl;
    }

    @Override
    public String ddlForUnitedDimensions(Dimension dim, List<String> satNames, String hubName, Boolean hasHierLink) {
        // CREATE-Statement with dimensionsTableName
        String pitName = hubName.replace("HUB_", "PIT_HUB_");
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + dim.getDimensionName() + " AS SELECT ";

        ddl = ddl + hubName + ".SQN, " + hubName + "." + dim.getBusinessKeyFieldName() + ", ";
        // Add parent if hub has hierarchical link
        if (hasHierLink) {
            ddl = ddl + "HAL_LINK_" + hubName.replace("HUB_", "") + "." + hubName + "_PARENT_SQN as PARENT_SQN, ";
        }

        ddl = ddl + pitName + ".LOAD_DATE as VALID_FROM, " + pitName + ".LOAD_END_DATE as VALID_TO, case when "
                + pitName + ".LOAD_END_DATE IS NOT NULL then 0 else 1 end as IS_VALID,";

        List<String> fields = dim.getFields();
        for (int i = 0; i < fields.size(); i++) {
            if (i == fields.size() - 1) {
                ddl = ddl + " " + fields.get(i);
            } else {
                ddl = ddl + " " + fields.get(i) + ",";
            }
        }

        // join of hub and pit table
        ddl = ddl + " FROM " + schemaName + "." + hubName + " join " + schemaName + "." + pitName + " on " + hubName
                + ".SQN = " + pitName + ".SQN";

        if (hasHierLink) {
            ddl = ddl + " LEFT JOIN " + schemaName + "." + hubName.replace("HUB_", "HAL_LINK_") + " ON " + schemaName
                    + "." + hubName + ".SQN = " + schemaName + "." + hubName.replace("HUB_", "HAL_LINK_") + "."
                    + hubName + "_SQN ";
        }

        // join of related satellites
        for (String satName : satNames) {
            ddl = ddl + " JOIN " + schemaName + "." + satName + " on " + schemaName + "." + hubName + ".SQN = "
                    + schemaName + "." + satName + ".SQN and " + schemaName + "." + pitName + "."
                    + satName.replace("SAT_", "") + "_LOAD_DATE = " + satName + ".LOAD_DATE";
        }
        return ddl;
    }

    @Override
    public String ddlForUnitedDimensionsNoHistory(Dimension dim, List<String> satNames, String hubName,
            Boolean hasHierLink) {
        // CREATE-Statement with dimensionsTableName
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + dim.getDimensionName() + " AS SELECT ";

        ddl = ddl + hubName + ".SQN, " + hubName + "." + dim.getBusinessKeyFieldName() + ", ";
        if (hasHierLink) {
            ddl = ddl + "HAL_LINK_" + hubName.replace("HUB_", "") + "." + hubName + "_PARENT_SQN as PARENT_SQN, ";
        }
        List<String> fields = dim.getFields();
        for (int i = 0; i < fields.size(); i++) {
            if (i == fields.size() - 1) {
                ddl = ddl + " " + fields.get(i);
            } else {
                ddl = ddl + " " + fields.get(i) + ",";
            }
        }
        // join of hub
        ddl = ddl + " FROM " + schemaName + "." + hubName;

        // join of related satellites
        for (String satName : satNames) {
            ddl = ddl + " JOIN " + schemaName + "." + satName + " on " + schemaName + "." + hubName + ".SQN = "
                    + schemaName + "." + satName + ".SQN";
        }

        // Add parent if hub has hierarchical link
        if (hasHierLink) {
            ddl = ddl + " LEFT JOIN " + schemaName + "." + hubName.replace("HUB_", "HAL_LINK_") + " ON " + schemaName
                    + "." + hubName + ".SQN = " + schemaName + "." + hubName.replace("HUB_", "HAL_LINK_") + "."
                    + hubName + "_SQN ";
        }
        // add where-statement
        for (int i = 0; i < satNames.size(); i++) {
            if (i == 0) {
                ddl = ddl + " where " + satNames.get(i) + ".LOAD_END_DATE IS NULL";
            } else {
                ddl = ddl + " and " + satNames.get(i) + ".LOAD_END_DATE IS NULL";
            }
        }
        return ddl;
    }

    @Override
    public String ddlForFactFromSat(FactFromSat fact) {
        String hubName = fact.getAssociatedHubName();
        String satName = fact.getOriginSatelliteName();
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + fact.getFactName() + " AS ";
        ddl = ddl + "SELECT ";
        List<String> fields = fact.getFieldsWithOrigin();
        for (int i = 0; i < fields.size(); i++) {
            ddl = ddl + fields.get(i);
            if (i < fields.size() - 1) {
                ddl = ddl + ", ";
            }
        }
        ddl = ddl + " FROM " + schemaName + "." + satName + " join " + schemaName + "." + hubName + " on " + satName
                + ".SQN = " + hubName + ".SQN";
        return ddl;
    }

    @Override
    public String ddlForFactsFromTaLink(FactFromTaLink fact) {
        String ddl = "CREATE OR REPLACE VIEW " + targetSchemaName + "." + fact.getFactName() + " AS SELECT ";
        List<String> fields = fact.getFieldsWithOrigin();
        for (int i = 0; i < fields.size(); i++) {
            ddl = ddl + fields.get(i);
            if (i < fields.size() - 1) {
                ddl = ddl + ",";
            }
            ddl = ddl + " ";
        }
        ddl = ddl + "FROM " + schemaName + "." + fact.getOriginTaLinkName() + " JOIN " + schemaName + "."
                + fact.getDescribingSatelliteName() + " ON " + fact.getOriginTaLinkName() + ".SQN = "
                + fact.getDescribingSatelliteName() + ".SQN";
        return ddl;
    }

}
