package de.viadee.dv;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.viadee.dv.service.SchemaExtractor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public abstract class AbstractIntegrationTest {

    @Autowired
    @Qualifier(value = "targetSchemaName")
    protected String targetSchemaName;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected SchemaExtractor schemaExtractor;

    public AbstractIntegrationTest() {
        super();
    }

    protected void dropViewsAndTables() {
        String viewQuery = "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = 'dvTarget' and TABLE_TYPE = 'VIEW'";
        List<String> views = jdbcTemplate.queryForList(viewQuery, String.class);
        for (String view : views) {
            jdbcTemplate.execute("DROP VIEW dvTarget." + view);
        }

        String tableQuery = "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = 'dvTarget' and TABLE_TYPE = 'BASE TABLE'";
        List<String> tables = jdbcTemplate.queryForList(tableQuery, String.class);
        for (String table : tables) {
            jdbcTemplate.execute("DROP TABLE dvTarget." + table);
        }
    }
}