package de.viadee.dv.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Link;
import de.viadee.dv.repository.HubDAO;
import de.viadee.dv.sql.SourceDMLCompositor;

public class HubDAOImpl implements HubDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SourceDMLCompositor sourceDMLCompositor;

    public List<Hub> getAllHubsFromSchema() {

        String sql = sourceDMLCompositor.queryAllHubsFromSchema();
        List<Hub> hubs = jdbcTemplate.query(sql, new RowMapper<Hub>() {

            public Hub mapRow(ResultSet resultSet, int i) throws SQLException {
                Hub newHub = new Hub();
                newHub.setTablename(resultSet.getString(1));
                newHub.setFields(getFieldsFromHub(newHub));
                newHub.setEquippedWithPitTable(checkForPitTable(newHub));
                newHub.hasHierLink(checkForHierLink(newHub));
                setBusinessKeyFieldFromHub(newHub);
                return newHub;
            }

        });
        return hubs;
    }

    private void setBusinessKeyFieldFromHub(Hub newHub) {
        for (String field : newHub.getFields()) {
            if (field.contains("_NUMBER")) {
                newHub.setBusinessKeyField(field);
            }
        }
    }

    public List<String> getFieldsFromHub(Hub hub) {
        String sql = sourceDMLCompositor.queryFieldsForHub(hub.getTablename());
        List<String> tables = (List<String>) jdbcTemplate.queryForList(sql, String.class);
        return tables;
    }

    public List<String> getRelatedHubNamesFromLink(Link link) {
        String sql = sourceDMLCompositor.queryRelatedHubNamesForLink(link.getTablename());
        List<String> hubNames = jdbcTemplate.queryForList(sql, String.class);
        return hubNames;
    }

    private boolean checkForPitTable(Hub newHub) {
        String sql = sourceDMLCompositor.queryIfHubHasPitTable(newHub.getTablename());
        Integer amount = jdbcTemplate.queryForObject(sql, Integer.class);
        if (amount == 1) {
            return true;
        }
        return false;
    }

    private boolean checkForHierLink(Hub newHub) {
        String sql = sourceDMLCompositor.queryIfHubHasHierLink(newHub.getTablename());
        Integer amount = jdbcTemplate.queryForObject(sql, Integer.class);
        // Hierarchical links do have more than 1 constraint to their hub!
        if (amount >= 1) {
            return true;
        }
        return false;
    }
}
