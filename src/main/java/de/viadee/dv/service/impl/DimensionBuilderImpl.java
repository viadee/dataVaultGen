package de.viadee.dv.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Schema;
import de.viadee.dv.repository.DimensionDAO;
import de.viadee.dv.service.DimensionBuilder;
import de.viadee.dv.service.supplement.HierarchyFlattener;
import de.viadee.dv.service.supplement.Materializer;
import de.viadee.dv.service.supplement.ViewEnhancer;

public class DimensionBuilderImpl implements DimensionBuilder {

    /**
     * Set though application.properties in applicationContext
     */
    private String modus;

    private String enhanceViews;

    private String history;

    private String persist;

    private String flatHierarchy;

    @Autowired
    private DimensionDAO dimensionDAO;

    @Autowired
    private ViewEnhancer viewEnhancer;

    @Autowired
    private Materializer materializer;

    @Autowired
    private HierarchyFlattener hierarchyFlattener;

    public void setModus(String modus) {
        this.modus = modus;
    }

    private List<Dimension> getDimensions(Schema schema) {
        List<Dimension> dimensions = new ArrayList<Dimension>();

        if (schema != null) {
            if (modus.equals("0")) {
                dimensions = dimensionDAO.createIndependentDimensions(schema);
            } else if (modus.equals("1")) {
                dimensions = dimensionDAO.createUnitedDimensions(schema);
            }

        }
        return dimensions;
    }

    @Override
    public void executeDimensionDDL(Schema schema) {
        List<Dimension> dimensions = getDimensions(schema);
        for (Dimension dim : dimensions) {
            dimensionDAO.executeDDLForDimension(dim);

            if (enhanceViews.equals("true")) {
                viewEnhancer.enhanceViewsForDimensions(dim);
            }

            if (flatHierarchy.equals("true")) {
                hierarchyFlattener.flattenHierarchyOfDimension(dim, history);
            }

            if (persist.equals("true")) {
                materializer.materializeDimension(dim, history, enhanceViews, flatHierarchy);
            }
        }

    }

    public void setEnhanceViews(String enhanceViews) {
        this.enhanceViews = enhanceViews;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setPersist(String persist) {
        this.persist = persist;
    }

    public void setFlatHierarchy(String flatHierarchy) {
        this.flatHierarchy = flatHierarchy;
    }

}
