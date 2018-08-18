package com.nrdc.adminPanel.jsonModel.jsonResponse;

public class ResponseViolationNegPoint {

    // TODO: 3/11/2018 change field name to negativePoint
    private String negPoint;
    private String usagePlateId;
    // TODO: 3/11/2018 change field name to vehicleViolationTypeId
    private String vehicleViolTypeId;
    private String violationTypeId;

    public String getNegPoint() {
        return negPoint;
    }

    public void setNegPoint(String negPoint) {
        this.negPoint = negPoint;
    }

    public String getUsagePlateId() {
        return usagePlateId;
    }

    public void setUsagePlateId(String usagePlateId) {
        this.usagePlateId = usagePlateId;
    }

    public String getVehicleViolTypeId() {
        return vehicleViolTypeId;
    }

    public void setVehicleViolTypeId(String vehicleViolTypeId) {
        this.vehicleViolTypeId = vehicleViolTypeId;
    }

    public String getViolationTypeId() {
        return violationTypeId;
    }

    public void setViolationTypeId(String violationTypeId) {
        this.violationTypeId = violationTypeId;
    }
}
