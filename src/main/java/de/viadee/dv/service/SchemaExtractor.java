package de.viadee.dv.service;

import de.viadee.dv.model.Schema;
import de.viadee.dv.repository.HubDAO;
import de.viadee.dv.repository.LinkDAO;
import de.viadee.dv.repository.SatelliteDAO;

/**
 * Interacts with database on metadata-level
 * 
 * @author B01
 */
public interface SchemaExtractor {

    /**
     * Builds "DOM" of given Schema. Interacts with database using {@link HubDAO}, {@link LinkDAO} and
     * {@link SatelliteDAO}
     * 
     * @return {@link Schema}
     */
    public Schema getSchema();

}
