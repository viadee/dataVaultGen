package de.viadee.dv.repository;

import java.util.List;

import de.viadee.dv.model.Fact;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.model.TransactionalLink;
import de.viadee.dv.service.FactBuilder;

/**
 * Helper Bean for {@link FactBuilder}. Inherits all operations regarding Facts.
 * 
 * @author B01
 *
 */
public interface FactDAO {

    /**
     * Returns all Facts that originate from {@link Satellite}
     * 
     * @param schema
     * @return
     */
    public List<FactFromSat> createFactsFromSatellites(Schema schema);

    /**
     * Returns all Facts that originate from {@link TransactionalLink}.
     * 
     * @param schema
     * @return
     */
    public List<FactFromTaLink> createFactsFromTaLinks(Schema schema);

    /**
     * Executes the previously generated DDL Statements for {@link FactFromSat}-Creation
     * 
     * @param dimension
     */
    public void executeDDLForFactFromSatelliteOrTaLink(Fact fact);

}
