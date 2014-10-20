package de.viadee.dv.repository;

import java.util.List;

import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.TransactionalLink;

/**
 * Interacts with database on metadata-level. Collects all operations in the context of the {@link Satellite}-Object.
 * 
 * @author B01
 */
public interface SatelliteDAO {

    /**
     * Scans database for Satellites related to a given {@link Hub}. Sets fields of satellite using
     * <code>getFields</code>
     * 
     * @param hub
     * @return List of {@link Satellite}
     */
    public List<Satellite> getSatellitesForHub(Hub hub);

    /**
     * Scans database for fields of a given Satellite. Used to determine fields of future DWH-Views.
     * 
     * @param satellite
     * @return List of {@link String} containing the field names.
     */
    public List<String> getFieldsForSatellite(Satellite satellite);

    /**
     * Scans database for the {@link Satellite} related to a given {@link TransactionalLink}. Sets fields of satellite
     * using <code>getFields</code> * @param link
     * 
     * @return
     */
    public Satellite getSatelliteForTransactionalLink(TransactionalLink link);
}
