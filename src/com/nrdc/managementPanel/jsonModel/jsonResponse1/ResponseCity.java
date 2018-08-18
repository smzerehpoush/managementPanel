package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseCity {

    private String cityId;
    private String cityName;
    //todo change this field to boolean
    private String isBig;

    public ResponseCity() {
    }

    public ResponseCity(String cityId, String cityName, String isBig) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.isBig = isBig;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIsBig() {
        return isBig;
    }

    public void setIsBig(String isBig) {
        this.isBig = isBig;
    }
}
