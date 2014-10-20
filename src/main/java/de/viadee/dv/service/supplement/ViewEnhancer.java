package de.viadee.dv.service.supplement;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Fact;
import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;

/**
 * Enhanced views of {@link Dimension}s and {@link Fact}s. A technical surrogate key depending on the SQN and LOAD_DATE
 * of a {@link Dimension} or {@link Fact} will be generated. Only works, if dwh.history is set to true.
 * 
 * @author B01
 *
 */
public interface ViewEnhancer {

    /**
     * Enhances the view of a given {@link Dimension}. If application parameter dwh.history is set to true, a technical
     * surrogate key depending on the SQN and LOAD_DATE will be added. I
     * 
     * @param dimensions
     */
    public void enhanceViewsForDimensions(Dimension dim);

    /**
     * Enhances the view of a given {@link FactFromTaLink} and adds a technical surrogate key if history-modus is
     * enabled.
     * 
     * @param factsFromTaLinks
     */
    public void enhanceViewsForFactsFromTaLinks(FactFromTaLink factFromTaLinks);

    /**
     * Enhances the view of given {@link FactFromSat} and adds a technical surrogate key if history-modus is enabled.
     * 
     * @param factFromSats
     */
    public void enhanceViewsForFactsFromSatellites(FactFromSat factFromSat);

}
