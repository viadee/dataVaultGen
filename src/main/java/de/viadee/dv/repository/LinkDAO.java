package de.viadee.dv.repository;

import java.util.List;

import de.viadee.dv.model.Link;
import de.viadee.dv.model.TransactionalLink;

/**
 * Interacts with database on metadata-level. Collects all operations in the context of the {@link Link}-Object.
 * 
 * @author B01
 */
public interface LinkDAO {

    /**
     * Reads all Links from given Schema, sets name of the link and fills list of table columns. Uses private method to
     * set fields of Link.
     */
    public List<Link> getAllLinksFromSchema();

    /**
     * Reads all {@link TransactionalLink} from a given Schema, sets name of the link end fills list of table Columns.
     * Uses private method to set fields of Link.
     * 
     * @return {@link List} of {@link TransactionalLink}s
     */
    public List<TransactionalLink> getAllTransActLinksFromSchema();

}
