package de.viadee.dv.model;

import java.util.List;

public class Link {

    // name of the Link
    private String tablename;

    // list of all referenced hubs
    private List<Hub> referencedHubs;

    private List<String> fields;

    public List<Hub> getReferencedHubs() {
        return referencedHubs;
    }

    public void setReferencedHubs(List<Hub> referencedHubs) {
        this.referencedHubs = referencedHubs;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

}
