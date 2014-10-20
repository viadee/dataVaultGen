package de.viadee.dv.model;

/**
 * Measurements originate from F-entries in {@link Satellite}. Fact-preferences of this type are unlike measurements
 * originating from {@link TransactionalLink}
 * 
 * @author B01
 *
 */
public class FactFromSat extends Fact {

    private String associatedHubName;

    private String originSatelliteName;

    public String getAssociatedHubName() {
        return associatedHubName;
    }

    public void setAssociatedHubName(String associatedHubName) {
        this.associatedHubName = associatedHubName;
    }

    public String getOriginSatelliteName() {
        return originSatelliteName;
    }

    public void setOriginSatelliteName(String originSatelliteName) {
        this.originSatelliteName = originSatelliteName;
    }

}
