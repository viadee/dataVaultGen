package de.viadee.dv.model;

import java.util.List;

public abstract class Fact {

    private String factName;

    private String ddlStatement;

    private List<String> fields;

    public String getDdlStatement() {
        return ddlStatement;
    }

    public void setDdlStatement(String ddlStatement) {
        this.ddlStatement = ddlStatement;
    }

    public String getFactName() {
        return factName;
    }

    public void setFactName(String factName) {
        this.factName = factName;
    }

    public List<String> getFieldsWithOrigin() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

}
