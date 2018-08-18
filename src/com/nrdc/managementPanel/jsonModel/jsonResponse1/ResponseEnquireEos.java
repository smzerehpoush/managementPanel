package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseEnquireEos {

    private String carColorId;
    private String carSysId;
    private String carUsageId;
    private String firstName;
    private String lastName;
    private String plateName;
    private String carType;
    private String carModel;
    private String nationalId;
    //change field name to engineNo
    private String enginNo;
    private String chassisNo;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getEnginNo() {
        return enginNo;
    }

    public void setEnginNo(String enginNo) {
        this.enginNo = enginNo;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getCarColorId() {
        return carColorId;
    }

    public void setCarColorId(String carColorId) {
        this.carColorId = carColorId;
    }

    public String getCarSysId() {
        return carSysId;
    }

    public void setCarSysId(String carSysId) {
        this.carSysId = carSysId;
    }

    public String getCarUsageId() {
        return carUsageId;
    }

    public void setCarUsageId(String carUsageId) {
        this.carUsageId = carUsageId;
    }
}
