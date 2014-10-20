package de.viadee.dv.sql.mySQL;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.sql.PITTableDMLCompositor;

public class PITTableDMLCompositorImpl implements PITTableDMLCompositor {

    @Autowired
    @Qualifier(value = "schemaName")
    private String schemaName;

    @Override
    public String selectLoadDatesFromSatellite(Satellite satellite, String key) {
        String dml = "SELECT DISTINCT LOAD_DATE FROM " + schemaName + "." + satellite.getTablename() + " WHERE SQN = "
                + key + " ORDER BY LOAD_DATE";
        return dml;
    }

    @Override
    public String selectKeysFromHub(Hub hub) {
        String dml = "SELECT DISTINCT SQN FROM " + schemaName + "." + hub.getTablename();
        return dml;
    }

    @Override
    public String createPITTable(Hub hub) {
        String create = "CREATE TABLE IF NOT EXISTS " + schemaName + ".PIT_" + hub.getTablename();

        create = create + "( SQN int(11), LOAD_DATE datetime, LOAD_END_DATE datetime, ";

        for (int i = 0; i < hub.getSatellites().size(); i++) {
            if (!hub.getSatellites().get(i).isFactSatellite()) {
                create = create + hub.getSatellites().get(i).getTablename().replace("SAT_", "")
                        + "_LOAD_DATE datetime,";
            }
        }
        create = create + " PRIMARY KEY (SQN,LOAD_DATE))";
        return create;

    }

    @Override
    public String persistInitialListOfLoadDates(String table, Date date, String key, String endDate) {
        String dml = "INSERT INTO " + schemaName + "." + table + " (SQN, LOAD_DATE, LOAD_END_DATE) VALUES ( " + key
                + ", '" + date + "', " + endDate + ")";
        return dml;
    }

    @Override
    public String persistSatelliteLoadDate(String tablename, Satellite sat, Date satDate, String key, Date loadDate) {
        String dml = "UPDATE " + schemaName + "." + tablename + " SET " + sat.getTablename().replace("SAT_", "")
                + "_LOAD_DATE = '" + satDate + "' WHERE LOAD_DATE = '" + loadDate + "' AND SQN = " + key;

        return dml;
    }

}
