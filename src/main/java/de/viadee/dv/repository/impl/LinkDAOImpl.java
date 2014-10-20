package de.viadee.dv.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.viadee.dv.model.Link;
import de.viadee.dv.model.TransactionalLink;
import de.viadee.dv.repository.LinkDAO;
import de.viadee.dv.sql.SourceDMLCompositor;

public class LinkDAOImpl implements LinkDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SourceDMLCompositor sourceDMLCompositor;

    /**
     * Scans database for fields of a given Link. Used to determine fields of future DWH-Views.
     * 
     * @param link
     * @return List of {@link String} containing the field names.
     */
    private List<String> getFields(Link link) {
        String sql = sourceDMLCompositor.queryFieldsForLink(link.getTablename());
        List<String> tables = (List<String>) jdbcTemplate.queryForList(sql, String.class);
        return tables;
    }

    public List<Link> getAllLinksFromSchema() {
        String sql = sourceDMLCompositor.queryAllLinksFromSchema();
        List<Link> links = jdbcTemplate.query(sql, new RowMapper<Link>() {

            public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
                Link newLink = new Link();
                newLink.setTablename(rs.getString(1));
                List<String> fields = getFields(newLink);
                newLink.setFields(fields);
                return newLink;
            }
        });
        return links;
    }

    @Override
    public List<TransactionalLink> getAllTransActLinksFromSchema() {
        String sql = sourceDMLCompositor.queryAllTransactionalLinksFromSchema();
        List<TransactionalLink> links = jdbcTemplate.query(sql, new RowMapper<TransactionalLink>() {

            public TransactionalLink mapRow(ResultSet rs, int rowNum) throws SQLException {
                TransactionalLink newLink = new TransactionalLink();
                newLink.setTablename(rs.getString(1));
                List<String> fields = getFields(newLink);
                newLink.setFields(fields);
                return newLink;
            }
        });
        return links;
    }

}
