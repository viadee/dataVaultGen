package de.viadee.dv.service.pit.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.service.pit.PITCreator;
import de.viadee.dv.sql.PITTableDMLCompositor;

public class PITCreatorImpl implements PITCreator {

    private static final Logger LOGGER = LogManager.getLogger(PITCreatorImpl.class.getName());

    @Autowired
    private PITTableDMLCompositor pitTableDMLCompositor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createPITTables(Schema schema) {

        for (Hub hub : schema.getHubs()) {
            if (hub.getSatellites().size() > 1 && hub.isEquippedWithPitTable() == false) {
                if (checkForFactFieldsInSatellites(hub)) {
                    createPITTableForHub(hub);
                    LOGGER.info("Created PIT-Table for Hub " + hub.getTablename());
                    hub.setEquippedWithPitTable(true);
                } else {
                    LOGGER.info("No PIT-Table for Hub " + hub.getTablename());
                }
            }
        }

    }

    /**
     * If a {@link Satellite} does only consist of fact fields, it should not be part of the PIT-Table.
     * 
     * @param hub
     * @return true, if {@link Hub} has at least two valid {@link Satellite}s
     */
    private boolean checkForFactFieldsInSatellites(Hub hub) {
        int amountOfValidSatellites = hub.getSatellites().size();
        for (Satellite sat : hub.getSatellites()) {

            if (sat.isFactSatellite()) {
                amountOfValidSatellites--;
            }
        }
        return amountOfValidSatellites > 1;
    }

    private void createPITTableForHub(Hub hub) {
        jdbcTemplate.execute(pitTableDMLCompositor.createPITTable(hub));
        List<String> keys = extractKeysFromHub(hub);

        /**
         * Extract PIT-Entries for specific SQN (key)
         */
        for (String key : keys) {
            ArrayList<Date> loadDates = extractLoadDatesFromHub(hub, key);
            persistAllLoadDates(loadDates, hub, key);
            persistLoadDatesForSatellites(loadDates, hub, key);
        }

    }

    /**
     * Persists load dates for entries in {@link Satellite}s
     * 
     * @param loadDates
     *            List of all possibles load dates for an {@link Hub}
     * @param hub
     * @param key
     */
    private void persistLoadDatesForSatellites(ArrayList<Date> loadDates, Hub hub, String key) {
        for (Satellite sat : hub.getSatellites()) {
            if (!sat.isFactSatellite()) {
                List<Date> satDates = extractLoadDatesFromSatellite(sat, key);
                for (Date satDate : satDates) {
                    Date satEndDate = calcEndDate(satDates, satDates.indexOf(satDate));
                    for (Date loadDate : loadDates) {
                        Date endDate = calcEndDate(loadDates, loadDates.indexOf(loadDate));

                        /**
                         * The important part: When does a satellite entry fit for a specific load date?
                         */
                        if (loadDate.after(satDate) || loadDate.equals(satDate)) {
                            if (satEndDate == null || endDate == null || endDate.before(satEndDate)
                                    || endDate.equals(satEndDate)) {
                                jdbcTemplate.execute(pitTableDMLCompositor.persistSatelliteLoadDate(
                                        "PIT_" + hub.getTablename(), sat, satDate, key, loadDate));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Persists all load dates for a given key (SQN), including the load_end_date
     * 
     * @param loadDates
     * @param hub
     * @param key
     */
    private void persistAllLoadDates(ArrayList<Date> loadDates, Hub hub, String key) {
        for (int i = 0; i < loadDates.size(); i++) {
            String endDate = calcEndDateString(loadDates, i);
            jdbcTemplate.execute(pitTableDMLCompositor.persistInitialListOfLoadDates("PIT_" + hub.getTablename(),
                    loadDates.get(i), key, endDate));
        }
    }

    /**
     * Calculates the end date of a given sqn-entry in {@link String} format
     * 
     * @param date
     * @return String of endDate
     */
    private String calcEndDateString(List<Date> loadDates, int index) {
        String endDateString = "null";

        Date endDate = calcEndDate(loadDates, index);
        if (endDate != null) {
            String formatedEndDateString = formatEndDateString(endDate);
            endDateString = "'" + formatedEndDateString + "'";
        }
        return endDateString;
    }

    private String formatEndDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * Calculates the end date of a given sqn-entry in {@link Date} format
     * 
     * @param date
     * @return End {@link Date}
     */
    private Date calcEndDate(List<Date> loadDates, int index) {
        Date endDate = null;
        if (index + 1 < loadDates.size()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loadDates.get(index + 1));
            cal.add(Calendar.SECOND, -1);
            endDate = cal.getTime();
        }
        return endDate;

    }

    /**
     * Uses {@link JdbcTemplate} to select all SQNs in a given {@link Hub}
     * 
     * @param hub
     * @return List of SQNs in given {@link Hub}
     */
    private List<String> extractKeysFromHub(Hub hub) {
        String dml = pitTableDMLCompositor.selectKeysFromHub(hub);
        List<String> keys = jdbcTemplate.queryForList(dml, String.class);
        return keys;
    }

    /**
     * Iterates over all {@link Satellite}s of a {@link Hub} to determine all load dates of a given SQN
     * 
     * @param hub
     * @param key
     * @return List of sorted load dates (String)
     */
    private ArrayList<Date> extractLoadDatesFromHub(Hub hub, String key) {
        ArrayList<Date> loadDates = new ArrayList<Date>();
        for (Satellite sat : hub.getSatellites()) {
            if (!sat.isFactSatellite()) {
                List<Date> satLoadDates = extractLoadDatesFromSatellite(sat, key);
                for (Date s : satLoadDates) {
                    if (!loadDates.contains(s)) {
                        loadDates.add(s);
                    }
                }
            }
        }
        Collections.sort(loadDates);
        return loadDates;
    }

    /**
     * Extracts all load dates of a {@link Satellite} for an given SQN
     * 
     * @param sat
     * @param key
     * @return {@link List} of sorted load {@link Date}s
     */
    private List<Date> extractLoadDatesFromSatellite(Satellite sat, String key) {
        String dml = pitTableDMLCompositor.selectLoadDatesFromSatellite(sat, key);
        List<Date> dates = jdbcTemplate.queryForList(dml, Date.class);
        return dates;
    }

}
