package de.viadee.dv.service.supplement;

import de.viadee.dv.model.Dimension;
import de.viadee.dv.model.Fact;

public interface Materializer {

    public void materializeDimension(Dimension dimension, String history, String enhanceViews, String flatHierarchy);

    public void materializeFact(Fact fact, String history, String enhanceViews, String modus);
}
