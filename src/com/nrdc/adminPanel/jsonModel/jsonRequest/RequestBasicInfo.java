package com.nrdc.adminPanel.jsonModel.jsonRequest;


public class RequestBasicInfo {
    private String policeCode;
    private String locationType;


    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {

        this.locationType = locationType;
    }

    @Override
    public String toString() {
        return "RequestBasicInfo{" +
                "policeCode='" + policeCode + '\'' +
                ", locationType='" + locationType + '\'' +
                '}';
    }
}
