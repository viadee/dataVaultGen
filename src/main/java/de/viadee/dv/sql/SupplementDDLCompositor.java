package de.viadee.dv.sql;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Fact;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.model.Hierarchy;

/**
 * With mode dwh.persist=true, tables are created from already existing views. Example given, for a {@link Dimension}
 * -View DIM_XXX a table or materialized view will be created. This will be named DIM_XXX_MAT. With persist=false, only
 * views will be generated including some transformations. They will be named like DIM_XXX_MAT, too. The transformations
 * are for both persistence-modes the same and result in less complicated queries.
 * 
 * @author B01
 *
 */
public interface SupplementDDLCompositor {

    /**
     * Concatenates DDL script to enhance {@link Dimension}-Views. If history is enabled (dwh.history=true), a hash
     * value is generated to represent the concatenated primary keys (SQN and LOAD_DATE) as one unique key. If history
     * is disabled, no enhancements will be made.
     * 
     * @param dim
     * @return DDL-Statement
     */
    public String ddlForDimViewEnhancement(Dimension dim);

    /**
     * Concatenates DDL script to enhance {@link Dimension}-Views if they inherit an hierarchy. With disabled history,
     * original SQNs are used to combine the facts with the dimension entries. With enabled history, the flattening can
     * not be executed yet.
     * 
     * @param dim
     * @param tree
     * @param maxHierLevel
     * @return DDL-Statement
     */
    public String ddlForHierarchyFlattening(Dimension dim, Hierarchy hierarchy);

    /**
     * Concatenates DDL script to check if a table does already exist for a given ({@link Dimension} or {@link Fact}).
     * 
     * @param viewname
     * @return DDL-Statement
     */
    public String dmlToCheckIfTableExists(String viewname);

    /**
     * Concatenates DDL script to materialize {@link FactFromTaLink}-Views.
     * 
     * @param fact
     * @return DDL-Statement
     */
    public String ddlForFactFromTaLinkViewEnhancement(FactFromTaLink fact);

    /**
     * Concatenates DDL script to materialize {@link FactFromSat}-Views
     * 
     * @param fact
     * @param dimName
     * @return DDL-Statement
     */
    public String ddlForFactFromSatViewEnhancement(FactFromSat fact, String dimName);

    /**
     * Concatenates DDL script to query all entries from given {@link Dimension}
     * 
     * @param dimensionName
     * @return DDL-Statement
     */
    public String dmlForSelectHierFieldsFromHierarchyDimension(String dimensionName);

    /**
     * Concatenates DDL script to materialize a given view (from {@link Fact} or {@link Dimension}).
     * 
     * @param factOrDimName
     *            name of the already existing dimension or fact view.
     * @return DDL-Statement
     */

    public String ddlForViewMaterializationAsSelect(String factOrDimName);

}
