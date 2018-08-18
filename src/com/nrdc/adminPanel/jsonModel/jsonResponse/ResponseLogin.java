package com.nrdc.adminPanel.jsonModel.jsonResponse;

/**
 * Created by JAVAd on 9/5/2015.
 */
public class ResponseLogin {

    private String token;
    private String firstName;
    private String lastName;
    private String policeTypeId;

    public ResponseLogin() {
    }

    public String getPoliceTypeId() {
        return policeTypeId;
    }

    public void setPoliceTypeId(String policeTypeId) {
        this.policeTypeId = policeTypeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
