package com.nrdc.managementPanel.jsonModel.jsonRequest;


public class RequestAddUser {
    private String token;
    private String password;
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String nationalId;
    private String policeCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    @Override
    public String toString() {
        return "RequestAddUser{" +
                "token='" + token + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", policeCode='" + policeCode + '\'' +
                '}';
    }
}
