package de.viadee.dv.sql.mySQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.viadee.dv.sql.SourceDMLCompositor;

public class SourceDMLCompositorImpl implements SourceDMLCompositor {

    @Autowired
    @Qualifier(value = "schemaName")
    private String schemaName;

    @Override
    public String querySatellitesFromHub(String tablename) {
        return "select TABLE_NAME from information_schema.REFERENTIAL_CONSTRAINTS where CONSTRAINT_SCHEMA = '"
                + schemaName + "' and TABLE_NAME LIKE 'SAT_%' and REFERENCED_TABLE_NAME = '" + tablename + "'";
    }

    @Override
    public String querySatellitesForTransactionalLink(String tablename) {
        return "select TABLE_NAME from information_schema.REFERENTIAL_CONSTRAINTS where CONSTRAINT_SCHEMA = '"
                + schemaName + "' and TABLE_NAME LIKE 'SAT_%' and REFERENCED_TABLE_NAME = '" + tablename + "' limit 1";
    }

    @Override
    public String queryFieldsForSatellite(String tablename) {
        return "select COLUMN_NAME from information_schema.COLUMNS where TABLE_SCHEMA = '" + schemaName
                + "' and TABLE_NAME like '" + tablename + "'";
    }

    @Override
    public String queryAllHubsFromSchema() {
        return "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = '" + schemaName
                + "' and TABLE_NAME LIKE 'HUB_%'";
    }

    @Override
    public String queryFieldsForHub(String tablename) {
        return "select COLUMN_NAME from information_schema.COLUMNS where TABLE_SCHEMA = '" + schemaName
                + "' and TABLE_NAME like '" + tablename + "'";
    }

    @Override
    public String queryRelatedHubNamesForLink(String tablename) {
        return "select REFERENCED_TABLE_NAME from information_schema.REFERENTIAL_CONSTRAINTS where CONSTRAINT_SCHEMA = '"
                + schemaName + "' and TABLE_NAME = '" + tablename + "'";
    }

    @Override
    public String queryFieldsForLink(String tablename) {
        return "select COLUMN_NAME from information_schema.COLUMNS where TABLE_SCHEMA = '" + schemaName
                + "' and TABLE_NAME like '" + tablename + "'";
    }

    @Override
    public String queryAllLinksFromSchema() {
        return "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = '" + schemaName
                + "' and TABLE_NAME LIKE 'LINK_%'";
    }

    @Override
    public String queryAllTransactionalLinksFromSchema() {
        return "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = '" + schemaName
                + "' and TABLE_NAME LIKE 'TA_LINK_%'";
    }

    @Override
    public String queryIfHubHasPitTable(String tablename) {
        return "select count(*) from information_schema.TABLES where TABLE_SCHEMA = '" + schemaName
                + "' and TABLE_NAME = 'PIT_" + tablename + "'";
    }

    @Override
    public String queryIfHubHasHierLink(String tablename) {
        return "select count(*) from information_schema.REFERENTIAL_CONSTRAINTS where CONSTRAINT_SCHEMA = '"
                + schemaName + "' and TABLE_NAME = 'HAL_LINK_" + tablename.replace("HUB_", "")
                + "' and REFERENCED_TABLE_NAME = '" + tablename + "'";
    }

}
