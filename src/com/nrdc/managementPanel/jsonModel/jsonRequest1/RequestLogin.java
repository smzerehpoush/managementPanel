package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestLogin {
    private String policeCode;
    private String password;

    public RequestLogin() {
    }

    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestLogin{" +
                "policeCode='" + policeCode + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
