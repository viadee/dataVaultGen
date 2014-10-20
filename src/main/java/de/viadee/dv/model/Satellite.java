package de.viadee.dv.model;

import java.util.List;

public class Satellite {

    private String tablename;

    private List<String> fields;

    private boolean factSatellite;

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

    public boolean isFactSatellite() {
        return factSatellite;
    }

    public void setFactSatellite(boolean factSatellite) {
        this.factSatellite = factSatellite;
    }

}
