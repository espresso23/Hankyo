package model;

import java.io.Serializable;

public class ReportType implements Serializable {
    private static final long serialVersionUID = 1L;
    private int reportTypeID;
    private String typeName;

    public ReportType() {
    }

    public ReportType(int reportTypeID, String typeName) {
        this.reportTypeID = reportTypeID;
        this.typeName = typeName;
    }

    public int getReportTypeID() {
        return reportTypeID;
    }

    public void setReportTypeID(int reportTypeID) {
        this.reportTypeID = reportTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}