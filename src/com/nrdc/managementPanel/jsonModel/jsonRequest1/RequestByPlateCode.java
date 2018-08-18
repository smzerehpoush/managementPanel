package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestByPlateCode {
    private String plateCode;

    public String getPlateCode() {
        return plateCode;
    }

    public void setPlateCode(String plateCode) {
        this.plateCode = plateCode;
    }

    @Override
    public String toString() {
        return "RequestByPlateCode{" +
                "plateCode='" + plateCode + '\'' +
                '}';
    }
}
