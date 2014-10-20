package de.viadee.dv.service.supplement;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Hierarchy;
import de.viadee.dv.model.Link;

/**
 * Provides functionality to flatten the hierarchy of a given {@link Dimension}. This {@link Dimension} needs an
 * hierarchical {@link Link}.
 * 
 * @author B01
 *
 */
public interface HierarchyFlattener {

    /**
     * Flattens the hierarchy of a given {@link Dimension} with hierarchical {@link Link}. Uses {@link Hierarchy} as
     * helping object
     * 
     * @param dim
     * @param history
     */
    public void flattenHierarchyOfDimension(Dimension dim, String history);
}
