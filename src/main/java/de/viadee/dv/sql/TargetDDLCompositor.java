package de.viadee.dv.sql;

import java.util.List;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Fact;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.model.Satellite;

public interface TargetDDLCompositor {

    /**
     * Concatenates DDL-Statements for each independent {@link Satellite} including the satellites history
     * 
     * @param dim
     * @param satName
     * @param hubName
     * @return DDL-Statement
     */
    public String ddlForindependentDimension(Dimension dim, String satName, String hubName, Boolean hasHierLink);

    /**
     * Concatenates DDL-Statements for each independent {@link Satellite} without the satellites history
     * 
     * @param dim
     * @param satName
     * @param hubName
     * @return DDL-Statement
     */
    public String ddlForIndependentDimensionNoHistory(Dimension dim, String satName, String hubName, Boolean hasHierLink);

    /**
     * Concatenates DDL for united {@link Satellite}s including their history
     * 
     * @param dim
     * @param satNames
     * @param hubName
     * @return DDL-Statement
     */
    public String ddlForUnitedDimensions(Dimension dim, List<String> satNames, String hubName, Boolean hasHierLink);

    /**
     * Concatenates DDL for united {@link Satellite}s without their history
     * 
     * @param dim
     * @param satNames
     * @param hubName
     * @return DDL-Statement
     */
    public String ddlForUnitedDimensionsNoHistory(Dimension dim, List<String> satNames, String hubName,
            Boolean hasHierLink);

    /**
     * Concatenates DDL script for {@link Fact}s that originate from {@link Satellite}s
     * 
     * @param fact
     * @return DDL-Statement
     */
    public String ddlForFactFromSat(FactFromSat fact);

    /**
     * Concatenates DDL script for {@link FactFromTaLink}
     * 
     * @param fact
     * @return DDL-Statement
     */
    public String ddlForFactsFromTaLink(FactFromTaLink fact);

}
