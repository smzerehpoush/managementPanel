package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseViolationDelivery {

    private int matchViolationDeliveryId;
    private String violationDeliveryId;
    private String violationTypeId;

    public ResponseViolationDelivery() {
    }

    public ResponseViolationDelivery(int matchViolationDeliveryId, String violationDeliveryId, String violationTypeId) {
        this.matchViolationDeliveryId = matchViolationDeliveryId;
        this.violationDeliveryId = violationDeliveryId;
        this.violationTypeId = violationTypeId;
    }

    public int getMatchViolationDeliveryId() {
        return matchViolationDeliveryId;
    }

    public void setMatchViolationDeliveryId(int matchViolationDeliveryId) {
        this.matchViolationDeliveryId = matchViolationDeliveryId;
    }

    public String getViolationDeliveryId() {
        return violationDeliveryId;
    }

    public void setViolationDeliveryId(String violationDeliveryId) {
        this.violationDeliveryId = violationDeliveryId;
    }

    public String getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(String violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
