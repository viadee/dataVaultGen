package de.viadee.dv.model;

import java.util.ArrayList;
import java.util.List;

public class Hub {

    private String tablename;

    private List<String> fields;

    private List<Satellite> satellites;

    private String businessKeyField;

    private boolean equippedWithPitTable;

    private boolean hasHierLink;

    public Hub() {
        this.fields = new ArrayList<String>();
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

    public List<Satellite> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<Satellite> satellites) {
        this.satellites = satellites;
    }

    public String getBusinessKeyField() {
        return businessKeyField;
    }

    public void setBusinessKeyField(String businessKeyField) {
        this.businessKeyField = businessKeyField;
    }

    public boolean isEquippedWithPitTable() {
        return equippedWithPitTable;
    }

    public void setEquippedWithPitTable(boolean equippedWithPitTable) {
        this.equippedWithPitTable = equippedWithPitTable;
    }

    public boolean hasHierLink() {
        return hasHierLink;
    }

    public void hasHierLink(boolean equippedWithHierLink) {
        this.hasHierLink = equippedWithHierLink;
    }

    // public List<Link> getLinks() {
    // return links;
    // }
    //
    // public void setLinks(List<Link> links) {
    // this.links = links;
    // }

}
