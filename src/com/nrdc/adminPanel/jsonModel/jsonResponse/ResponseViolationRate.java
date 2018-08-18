package com.nrdc.adminPanel.jsonModel.jsonResponse;

public class ResponseViolationRate {

    // TODO: 3/11/2018 change field type to boolean
    private String isBig;
    // TODO: 3/11/2018 change field type to boolean
    private String isInternal;
    private String violationPrice;
    private String violationRateId;
    private String violationTypeId;

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

    public String getViolationPrice() {
        return violationPrice;
    }

    public void setViolationPrice(String violationPrice) {
        this.violationPrice = violationPrice;
    }

    public String getViolationRateId() {
        return violationRateId;
    }

    public void setViolationRateId(String violationRateId) {
        this.violationRateId = violationRateId;
    }

    public String getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(String violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
