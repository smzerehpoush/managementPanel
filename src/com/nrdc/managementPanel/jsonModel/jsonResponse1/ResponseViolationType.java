package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseViolationType {

    private String countable;
    private String violationType;
    private String violationTypeId;

    public String getCountable() {
        return countable;
    }

    public void setCountable(String countable) {
        this.countable = countable;
    }

    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public String getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(String violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
