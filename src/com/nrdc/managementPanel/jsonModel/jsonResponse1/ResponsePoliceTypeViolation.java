package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponsePoliceTypeViolation {

    private String policeTypeId;
    private String policeTypeViolationId;
    private String violationTypeId;

    public ResponsePoliceTypeViolation() {
    }

    public ResponsePoliceTypeViolation(String policeTypeId, String policeTypeViolationId, String violationTypeId) {
        this.policeTypeId = policeTypeId;
        this.policeTypeViolationId = policeTypeViolationId;
        this.violationTypeId = violationTypeId;
    }

    public String getPoliceTypeId() {
        return policeTypeId;
    }

    public void setPoliceTypeId(String policeTypeId) {
        this.policeTypeId = policeTypeId;
    }

    public String getPoliceTypeViolationId() {
        return policeTypeViolationId;
    }

    public void setPoliceTypeViolationId(String policeTypeViolationId) {
        this.policeTypeViolationId = policeTypeViolationId;
    }

    public String getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(String violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
