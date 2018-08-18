package com.nrdc.adminPanel.jsonModel.jsonResponse;

public class ResponseViolationViolation {
    private String violationTypeId1;
    private String violationTypeId2;
    private Integer violationViolationId;

    public ResponseViolationViolation() {
    }

    public ResponseViolationViolation(String violationTypeId1, String violationTypeId2, Integer violationViolationId) {
        this.violationTypeId1 = violationTypeId1;
        this.violationTypeId2 = violationTypeId2;
        this.violationViolationId = violationViolationId;
    }

    public String getViolationTypeId1() {
        return violationTypeId1;
    }

    public void setViolationTypeId1(String violationTypeId1) {
        this.violationTypeId1 = violationTypeId1;
    }

    public String getViolationTypeId2() {
        return violationTypeId2;
    }

    public void setViolationTypeId2(String violationTypeId2) {
        this.violationTypeId2 = violationTypeId2;
    }

    public Integer getViolationViolationId() {
        return violationViolationId;
    }

    public void setViolationViolationId(Integer violationViolationId) {
        this.violationViolationId = violationViolationId;
    }
}
