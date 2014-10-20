package de.viadee.dv.sql;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Link;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.model.TransactionalLink;

public interface SourceDMLCompositor {

    // public String getTableNameFieldInDataBase();

    /**
     * Creates DDL for extraction of describing satellites for {@link Hub}s
     * 
     * @param tablename
     *            of {@link Hub}
     * @return DDL-Statement
     */
    public String querySatellitesFromHub(String tablename);

    /**
     * DDL to extract describing satellites for {@link TransactionalLink}s
     * 
     * @param tablename
     *            of {@link TransactionalLink}
     * @return DDL-Statement
     */
    public String querySatellitesForTransactionalLink(String tablename);

    /**
     * DDL to extract fields of a given {@link Satellite}
     * 
     * @param tablename
     *            of {@link Satellite}
     * @return DDL-Statement
     */
    public String queryFieldsForSatellite(String tablename);

    /**
     * DDL to extract all hub of a {@link Schema}
     * 
     * @return DDL-Statement
     */
    public String queryAllHubsFromSchema();

    /**
     * DDL to extract all fields of a given {@link Hub}
     * 
     * @param tablename
     *            of {@link Hub}
     * @return DDL-Statement
     */
    public String queryFieldsForHub(String tablename);

    /**
     * DDL to extract related {@link Hub}s of a {@link Link}
     * 
     * @param tablename
     *            of {@link Link}
     * @return DDL-Statement
     */
    public String queryRelatedHubNamesForLink(String tablename);

    /**
     * DDL to check if a given {@link Hub} has a PIT-Table
     * 
     * @param tablename
     * 
     * @return DDL-Statement
     */
    public String queryIfHubHasPitTable(String tablename);

    /**
     * DDL to extract all fields of a given {@link Link}
     * 
     * @param tablename
     *            of {@link Link}
     * @return DDL-Statement
     */
    public String queryFieldsForLink(String tablename);

    /**
     * DDL to extract all {@link Link}s of a given {@link Schema}
     * 
     * @return
     */
    public String queryAllLinksFromSchema();

    /**
     * DDL to extract all {@link TransactionalLink}s of a given {@link Schema}
     * 
     * @return
     */
    public String queryAllTransactionalLinksFromSchema();

    /**
     * DDL to check if a given {@link Hub} has a {@link HierLink}
     * 
     * @param tablename
     * @return true or false
     */
    public String queryIfHubHasHierLink(String tablename);

}
