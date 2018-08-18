/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nrdc.adminPanel.common;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author JAVAd
 */
public class ResultSymfaData {
    private String $id;
    @JsonProperty("ReceptionId")
    private String receptionId;
    @JsonProperty("PetrolLabelSerial")
    private String petrolLabelSerial;
    @JsonProperty("VINNO")
    private String vinNo;
    @JsonProperty("NationalPlaque")
    private String nationalPlaque;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("NationalCode")
    private String nationalCode;
    @JsonProperty("CellPhone")
    private String cellPhone;
    @JsonProperty("RegisterDate")
    private String registerDate;
    @JsonProperty("Date")
    private String date;
    @JsonProperty("Retest")
    private String retest;
    @JsonProperty("ChassisNo")
    private String chassisNo;
    @JsonProperty("MotorNo")
    private String motorNo;
    @JsonProperty("Model")
    private String model;
    @JsonProperty("System")
    private String system;
    @JsonProperty("Tip")
    private String tip;
    @JsonProperty("TrackCode")
    private String trackCode;
    @JsonProperty("TRNo")
    private String trNo;
    @JsonProperty("TRName")
    private String trName;
    private String province;
    private String city;
    private String town;

    public ResultSymfaData() {
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(String receptionId) {
        this.receptionId = receptionId;
    }

    public String getPetrolLabelSerial() {
        return petrolLabelSerial;
    }

    public void setPetrolLabelSerial(String petrolLabelSerial) {
        this.petrolLabelSerial = petrolLabelSerial;
    }

    public String getVinNo() {
        return vinNo;
    }

    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }

    public String getNationalPlaque() {
        return nationalPlaque;
    }

    public void setNationalPlaque(String nationalPlaque) {
        this.nationalPlaque = nationalPlaque;
    }

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

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRetest() {
        return retest;
    }

    public void setRetest(String retest) {
        this.retest = retest;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getMotorNo() {
        return motorNo;
    }

    public void setMotorNo(String motorNo) {
        this.motorNo = motorNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public String getTrNo() {
        return trNo;
    }

    public void setTrNo(String trNo) {
        this.trNo = trNo;
    }

    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
