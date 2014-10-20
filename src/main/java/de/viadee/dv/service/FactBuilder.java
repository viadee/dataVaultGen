package de.viadee.dv.service;

import de.viadee.dv.model.Satellite;
import de.viadee.dv.model.Schema;
import de.viadee.dv.model.TransactionalLink;

/**
 * Creates views acting as Fact tables
 * 
 * @author B01
 *
 */
public interface FactBuilder {

    /**
     * Executes generated DDL script to create view for fact tables. The measurements of these fact tables originate
     * from {@link TransactionalLink} or are embedded in {@link Satellite}
     * 
     * @param schema
     */
    public void executeFactDDL(Schema schema);
}
