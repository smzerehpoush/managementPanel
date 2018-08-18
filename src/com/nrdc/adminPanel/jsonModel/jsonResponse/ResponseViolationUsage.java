package com.nrdc.adminPanel.jsonModel.jsonResponse;

public class ResponseViolationUsage {

    private int matchViolationUsage;
    private String vehicleUsageId;
    private String violationTypeId;
    private String pkVehicleUsage;

    public ResponseViolationUsage() {
    }

    public ResponseViolationUsage(int matchViolationUsage, String vehicleUsageId, String violationTypeId, String pkVehicleUsage) {
        this.matchViolationUsage = matchViolationUsage;
        this.vehicleUsageId = vehicleUsageId;
        this.violationTypeId = violationTypeId;
        this.pkVehicleUsage = pkVehicleUsage;
    }

    public String getPkVehicleUsage() {
        return pkVehicleUsage;
    }

    public void setPkVehicleUsage(String pkVehicleUsage) {
        this.pkVehicleUsage = pkVehicleUsage;
    }

    public int getMatchViolationUsage() {
        return matchViolationUsage;
    }

    public void setMatchViolationUsage(int matchViolationUsage) {
        this.matchViolationUsage = matchViolationUsage;
    }

    public String getVehicleUsageId() {
        return vehicleUsageId;
    }

    public void setVehicleUsageId(String vehicleUsageId) {
        this.vehicleUsageId = vehicleUsageId;
    }

    public String getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(String violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
