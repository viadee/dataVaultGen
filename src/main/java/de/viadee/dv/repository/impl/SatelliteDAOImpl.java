package de.viadee.dv.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.TransactionalLink;
import de.viadee.dv.repository.SatelliteDAO;
import de.viadee.dv.sql.SourceDMLCompositor;

public class SatelliteDAOImpl implements SatelliteDAO {

    private static final Logger LOGGER = LogManager.getLogger(SatelliteDAOImpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SourceDMLCompositor sourceDMLCompositor;

    public List<Satellite> getSatellitesForHub(Hub hub) {
        String sql = sourceDMLCompositor.querySatellitesFromHub(hub.getTablename());
        try {
            List<Satellite> satellites = jdbcTemplate.query(sql, new RowMapper<Satellite>() {

                public Satellite mapRow(ResultSet resultSet, int i) throws SQLException {
                    Satellite satellite = new Satellite();
                    satellite.setTablename(resultSet.getString(1));
                    satellite.setFields(getFieldsForSatellite(satellite));
                    satellite.setFactSatellite(checkForFactFieldsInSatellite(satellite));
                    return satellite;
                }

            });
            return satellites;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Satellite getSatelliteForTransactionalLink(TransactionalLink link) {
        String sql = sourceDMLCompositor.querySatellitesForTransactionalLink(link.getTablename());

        try {
            Satellite satellite = jdbcTemplate.queryForObject(sql, new RowMapper<Satellite>() {

                @Override
                public Satellite mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Satellite sat = new Satellite();
                    sat.setTablename(rs.getString(1));
                    sat.setFields(getFieldsForSatellite(sat));
                    return sat;
                }
            });
            return satellite;

        } catch (DataAccessException e) {
            LOGGER.debug(e);
            return null;
        }
    }

    private boolean checkForFactFieldsInSatellite(Satellite satellite) {
        boolean factFields = true;
        for (String field : satellite.getFields()) {
            if (!(field.startsWith("F_") || field.equals("SQN") || field.equals("LOAD_DATE")
                    || field.equals("LOAD_END_DATE") || field.equals("REC_SOURCE"))) {
                factFields = false;
            }
        }
        return factFields;
    }

    public List<String> getFieldsForSatellite(Satellite satellite) {
        String sql = sourceDMLCompositor.queryFieldsForSatellite(satellite.getTablename());
        List<String> tables = (List<String>) jdbcTemplate.queryForList(sql, String.class);
        return tables;
    }

}
