package com.nrdc.managementPanel.jsonModel.jsonResponse;

/**
 * Created by jvd.karimi on 12/24/2017.
 */
public class ResponseVehicleUsage {

    private String vehicleUsageCode;
    private String vehicleUsageId;

    private String vehicleUsageName;

    public String getVehicleUsageCode() {
        return vehicleUsageCode;
    }

    public void setVehicleUsageCode(String vehicleUsageCode) {
        this.vehicleUsageCode = vehicleUsageCode;
    }

    public String getVehicleUsageId() {
        return vehicleUsageId;
    }

    public void setVehicleUsageId(String vehicleUsageId) {
        this.vehicleUsageId = vehicleUsageId;
    }

    public String getVehicleUsageName() {
        return vehicleUsageName;
    }

    public void setVehicleUsageName(String vehicleUsageName) {
        this.vehicleUsageName = vehicleUsageName;
    }
}
