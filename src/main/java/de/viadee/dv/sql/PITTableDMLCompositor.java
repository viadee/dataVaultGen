package de.viadee.dv.sql;

import java.util.Date;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;

/**
 * Provides DML-Statements to detect load dates and sqns (keys) for PIT-Table creation
 * 
 * @author B01
 *
 */
public interface PITTableDMLCompositor {

    /**
     * Selects all Loaddates of a given satellite with a given key
     * 
     * @param satellite
     * @param key
     *            (SQN from Hub)
     * @return DML-Statement
     */
    public String selectLoadDatesFromSatellite(Satellite satellite, String key);

    /**
     * Selects all SQNs of a given {@link Hub}
     * 
     * @param hub
     * @return DML-Statement
     */
    public String selectKeysFromHub(Hub hub);

    /**
     * Creates PIT Table for {@link Hub}
     * 
     * @param hub
     * @return
     */
    public String createPITTable(Hub hub);

    /**
     * Persists the initial list of possible load dates of a {@link Hub} and a specific key
     * 
     * @param endDate
     * @param dates
     * @return DML-Statement to persist initial list of load dates.
     */
    public String persistInitialListOfLoadDates(String table, Date date, String key, String endDate);

    /**
     * Persists entry for Satellite
     * 
     * @param pitTableName
     * @param satDate
     * @param key
     * @param loadDate
     * @param formatEndDateString
     * @return DML-Statement to persist a single load date
     */
    public String persistSatelliteLoadDate(String pitTableName, Satellite sat, Date satDate, String key, Date loadDate);
}
