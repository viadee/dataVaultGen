package de.viadee.dv.model;

/**
 * Measurements originate from {@link TransactionalLink}. Fact-preferences of this type are unlike measurements
 * originating from F-entries in {@link Satellite}
 * 
 * @author B01
 *
 */
public class FactFromTaLink extends Fact {

    private String originTaLinkName;

    private String describingSatelliteName;

    public String getDescribingSatelliteName() {
        return describingSatelliteName;
    }

    public void setDescribingSatelliteName(String describingSatelliteName) {
        this.describingSatelliteName = describingSatelliteName;
    }

    public String getOriginTaLinkName() {
        return originTaLinkName;
    }

    public void setOriginTaLinkName(String originTaLinkName) {
        this.originTaLinkName = originTaLinkName;
    }
}
