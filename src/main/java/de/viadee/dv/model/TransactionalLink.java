package de.viadee.dv.model;

/**
 * A {@link TransactionalLink} only has one describing {@link Satellite}.
 * 
 * @author B01
 *
 */
public class TransactionalLink extends Link {

    private Satellite referencedSatellite;

    public Satellite getReferencedSatellite() {
        return referencedSatellite;
    }

    public void setReferencedSatellite(Satellite referencedSatellite) {
        this.referencedSatellite = referencedSatellite;
    }

}
