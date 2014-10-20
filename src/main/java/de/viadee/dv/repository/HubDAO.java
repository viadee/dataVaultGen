package de.viadee.dv.repository;

import java.util.List;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Link;

/**
 * Interacts with database on metadata-level. Collects all operations in the context of the {@link Hub}-Object.
 * 
 * @author B01
 */
public interface HubDAO {

    /**
     * Scans database for Hubs. Sets tablename and uses <code>getFieldsFromHub</code> to extract field names.
     * 
     * @return List of {@link Hub}
     */
    public List<Hub> getAllHubsFromSchema();

    /**
     * Reads fields from given Hub. Used to determine fields of future DWH-Views.
     * 
     * @param hub
     * @return List of {@link String}
     */
    public List<String> getFieldsFromHub(Hub hub);

    /**
     * Scans the database for hubs that are related to a given {@link Link}
     * 
     * @param link
     * @return List of {@link String}
     */
    public List<String> getRelatedHubNamesFromLink(Link link);

}
