package de.viadee.dv.service.pit;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;

/**
 * Access the source database to add pit-tables to {@link Hub}s with several {@link Satellite}s
 * 
 * @author B01
 *
 */
public interface PITCreator {

    /**
     * Method to create Point in time-Tables for {@link Hub}s in {@link Schema}
     * 
     * @param schema
     */
    public void createPITTables(Schema schema);

}
