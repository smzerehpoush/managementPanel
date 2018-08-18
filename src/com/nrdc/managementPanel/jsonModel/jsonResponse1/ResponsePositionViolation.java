package com.nrdc.managementPanel.jsonModel.jsonResponse;


public class ResponsePositionViolation {

    // TODO: 3/11/2018 change field type
    private String external;
    // TODO: 3/11/2018 change field type to boolean
    private String isBig;
    // TODO: 3/11/2018 change field type to boolean
    private String isInternal;
    private String positionViolationId;
    private String violationTypeId;

    public ResponsePositionViolation() {
    }

    public ResponsePositionViolation(String external, String isBig, String isInternal, String positionViolationId, String violationTypeId) {
        this.external = external;
        this.isBig = isBig;
        this.isInternal = isInternal;
        this.positionViolationId = positionViolationId;
        this.violationTypeId = violationTypeId;
    }

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public String getIsBig() {
        return isBig;
    }

    public void setIsBig(String isBig) {
        this.isBig = isBig;
    }

    public String getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(String isInternal) {
        this.isInternal = isInternal;
    }

    public String getPositionViolationId() {
        return positionViolationId;
    }

    public void setPositionViolationId(String positionViolationId) {
        this.positionViolationId = positionViolationId;
    }

    public String getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(String violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
