package com.nrdc.adminPanel.jsonModel.jsonResponse;

public class ResponsePlateUsage {

    private String plateTypeId;
    private String usagePlateId;
    private String vehicleUsageId;

    public ResponsePlateUsage(String plateTypeId, String usagePlateId, String vehicleUsageId) {
        this.plateTypeId = plateTypeId;
        this.usagePlateId = usagePlateId;
        this.vehicleUsageId = vehicleUsageId;
    }

    public ResponsePlateUsage() {
    }

    public String getPlateTypeId() {
        return plateTypeId;
    }

    public void setPlateTypeId(String plateTypeId) {
        this.plateTypeId = plateTypeId;
    }

    public String getUsagePlateId() {
        return usagePlateId;
    }

    public void setUsagePlateId(String usagePlateId) {
        this.usagePlateId = usagePlateId;
    }

    public String getVehicleUsageId() {
        return vehicleUsageId;
    }

    public void setVehicleUsageId(String vehicleUsageId) {
        this.vehicleUsageId = vehicleUsageId;
    }
}
