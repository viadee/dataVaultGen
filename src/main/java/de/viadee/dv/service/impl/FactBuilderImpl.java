package de.viadee.dv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.model.FactFromSat;
import de.viadee.dv.model.FactFromTaLink;
import de.viadee.dv.model.Schema;
import de.viadee.dv.repository.FactDAO;
import de.viadee.dv.service.FactBuilder;
import de.viadee.dv.service.supplement.Materializer;
import de.viadee.dv.service.supplement.ViewEnhancer;

public class FactBuilderImpl implements FactBuilder {

    @Autowired
    private FactDAO factDAO;

    private String enhanceViews;

    private String modus;

    private String history;

    private String persist;

    @Autowired
    private ViewEnhancer viewEnhancer;

    @Autowired
    private Materializer materializer;

    @Override
    public void executeFactDDL(Schema schema) {

        List<FactFromTaLink> factsFromTaLinks = factDAO.createFactsFromTaLinks(schema);
        for (FactFromTaLink factTaLink : factsFromTaLinks) {
            factDAO.executeDDLForFactFromSatelliteOrTaLink(factTaLink);

            if (enhanceViews.equals("true") && modus.equals("1")) {
                viewEnhancer.enhanceViewsForFactsFromTaLinks(factTaLink);
            }

            if (persist.equals("true")) {
                materializer.materializeFact(factTaLink, history, enhanceViews, modus);
            }

        }

        List<FactFromSat> factFromSats = factDAO.createFactsFromSatellites(schema);
        for (FactFromSat factSat : factFromSats) {
            factDAO.executeDDLForFactFromSatelliteOrTaLink(factSat);
            if (enhanceViews.equals("true") && modus.equals("1")) {
                viewEnhancer.enhanceViewsForFactsFromSatellites(factSat);
            }

            if (persist.equals("true")) {
                materializer.materializeFact(factSat, history, enhanceViews, modus);
            }
        }

    }

    public void setEnhanceViews(String enhanceViews) {
        this.enhanceViews = enhanceViews;
    }

    public void setModus(String modus) {
        this.modus = modus;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setPersist(String persist) {
        this.persist = persist;
    }

}
