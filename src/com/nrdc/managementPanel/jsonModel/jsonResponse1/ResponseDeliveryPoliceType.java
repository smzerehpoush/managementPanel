package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseDeliveryPoliceType {
    private String matchDeliveryPoliceId;
    private String policeTypeId;
    private String violationDeliveryId;

    public ResponseDeliveryPoliceType() {
    }

    public ResponseDeliveryPoliceType(String matchDeliveryPoliceId, String policeTypeId, String violationDeliveryId) {
        this.matchDeliveryPoliceId = matchDeliveryPoliceId;
        this.policeTypeId = policeTypeId;
        this.violationDeliveryId = violationDeliveryId;
    }

    public String getMatchDeliveryPoliceId() {
        return matchDeliveryPoliceId;
    }

    public void setMatchDeliveryPoliceId(String matchDeliveryPoliceId) {
        this.matchDeliveryPoliceId = matchDeliveryPoliceId;
    }

    public String getPoliceTypeId() {
        return policeTypeId;
    }

    public void setPoliceTypeId(String policeTypeId) {
        this.policeTypeId = policeTypeId;
    }

    public String getViolationDeliveryId() {
        return violationDeliveryId;
    }

    public void setViolationDeliveryId(String violationDeliveryId) {
        this.violationDeliveryId = violationDeliveryId;
    }
}
