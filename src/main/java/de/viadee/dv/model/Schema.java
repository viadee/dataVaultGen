package de.viadee.dv.model;

import java.util.ArrayList;
import java.util.List;

public class Schema {

    private List<Hub> hubs;

    private List<Link> links;

    private List<TransactionalLink> transactionalLinks;

    private boolean schemaIsValid;

    public Schema() {
        this.hubs = new ArrayList<Hub>();
    }

    public List<Hub> getHubs() {
        return hubs;
    }

    public void setHubs(List<Hub> hubs) {
        this.hubs = hubs;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<TransactionalLink> getTransactionalLinks() {
        return transactionalLinks;
    }

    public void setTransactionalLinks(List<TransactionalLink> transactionalLinks) {
        this.transactionalLinks = transactionalLinks;
    }

    public boolean isSchemaIsValid() {
        return schemaIsValid;
    }

    public void setSchemaIsValid(boolean schemaIsValid) {
        this.schemaIsValid = schemaIsValid;
    }

}
