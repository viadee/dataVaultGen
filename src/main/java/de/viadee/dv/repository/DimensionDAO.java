package de.viadee.dv.repository;

import java.util.List;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Hub;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;

/**
 * This interface provides methods for concatenating DDL-Statements and for selection of future dimension fields. <br>
 * There is a distinction between independent and united dimensions.
 * 
 * @author B01
 *
 */
public interface DimensionDAO {

    /**
     * The dimensions are independent. Example: One {@link Hub} has multiple {@link Satellite}s. For each satellite, an
     * independent {@link Dimension} will be generated.
     * 
     * @param b
     * 
     * @return list of future dimensions
     */
    public List<Dimension> createIndependentDimensions(Schema schema);

    /**
     * Related {@link Dimension}s will be united. Example: One {@link Hub} has multiple {@link Satellite}s. For all
     * Satellites, one Dimension will be generated.
     * 
     * @param b
     * 
     * @return list of future dimensions
     */
    public List<Dimension> createUnitedDimensions(Schema schema);

    /**
     * Executes the previously generated DDL Statements for Dimension-Creation
     * 
     * @param dimension
     */
    public void executeDDLForDimension(Dimension dimension);

}
