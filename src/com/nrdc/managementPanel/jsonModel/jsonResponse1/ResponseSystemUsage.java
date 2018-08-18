package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseSystemUsage {

    private Integer matchSystemUsage;
    private String vehicleSystemId;
    private String vehicleUsageId;

    public ResponseSystemUsage(Integer matchSystemUsage, String vehicleSystemId, String vehicleUsageId) {
        this.matchSystemUsage = matchSystemUsage;
        this.vehicleSystemId = vehicleSystemId;
        this.vehicleUsageId = vehicleUsageId;
    }

    public ResponseSystemUsage() {
    }

    public Integer getMatchSystemUsage() {
        return matchSystemUsage;
    }

    public void setMatchSystemUsage(Integer matchSystemUsage) {
        this.matchSystemUsage = matchSystemUsage;
    }

    public String getVehicleSystemId() {
        return vehicleSystemId;
    }

    public void setVehicleSystemId(String vehicleSystemId) {
        this.vehicleSystemId = vehicleSystemId;
    }

    public String getVehicleUsageId() {
        return vehicleUsageId;
    }

    public void setVehicleUsageId(String vehicleUsageId) {
        this.vehicleUsageId = vehicleUsageId;
    }
}
