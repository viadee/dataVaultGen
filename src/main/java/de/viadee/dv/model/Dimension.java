package de.viadee.dv.model;

import java.util.List;

public class Dimension {

    private String dimensionName;

    private String businessKeyFieldName;

    private List<String> fields;

    private String ddlStatement;

    private Boolean isHierarchical;

    public String getBusinessKeyFieldName() {
        return businessKeyFieldName;
    }

    public void setBusinessKeyFieldName(String businessKeyFieldName) {
        this.businessKeyFieldName = businessKeyFieldName;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getDdlStatement() {
        return ddlStatement;
    }

    public void setDdlStatement(String ddlStatement) {
        this.ddlStatement = ddlStatement;
    }

    public Boolean isHierarchical() {
        return isHierarchical;
    }

    public void setHierarchical(Boolean isHierarchical) {
        this.isHierarchical = isHierarchical;
    }

}
