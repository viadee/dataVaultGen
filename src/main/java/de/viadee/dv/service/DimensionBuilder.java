package de.viadee.dv.service;

import de.viadee.dv.model.Schema;

/**
 * Creates Views acting as Dimensions.
 * 
 * @author B01
 *
 */
public interface DimensionBuilder {

    public void executeDimensionDDL(Schema schema);
}
