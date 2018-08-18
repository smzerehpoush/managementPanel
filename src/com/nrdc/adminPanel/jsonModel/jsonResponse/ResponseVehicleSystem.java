package com.nrdc.adminPanel.jsonModel.jsonResponse;

/**
 * Created by jvd.karimi on 12/24/2017.
 */
public class ResponseVehicleSystem {

    private String enfSystemCode;

    private String vehicleSystemCode;

    private String vehicleSystemId;

    private String vehicleSystemName;


    public String getEnfSystemCode() {
        return enfSystemCode;
    }

    public void setEnfSystemCode(String enfSystemCode) {
        this.enfSystemCode = enfSystemCode;
    }

    public String getVehicleSystemCode() {
        return vehicleSystemCode;
    }

    public void setVehicleSystemCode(String vehicleSystemCode) {
        this.vehicleSystemCode = vehicleSystemCode;
    }

    public String getVehicleSystemId() {
        return vehicleSystemId;
    }

    public void setVehicleSystemId(String vehicleSystemId) {
        this.vehicleSystemId = vehicleSystemId;
    }

    public String getVehicleSystemName() {
        return vehicleSystemName;
    }

    public void setVehicleSystemName(String vehicleSystemName) {
        this.vehicleSystemName = vehicleSystemName;
    }
}
