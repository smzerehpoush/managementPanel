package com.nrdc.managementPanel.jsonModel.jsonRequest;


public class RequestEditUser {
    private Long fkUserId;
    private String username;
    private Boolean isActive;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String nationalId;
    private String policeCode;
    private Long fkSystemId;

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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
        return "RequestEditUser{" +
                "fkUserId=" + fkUserId +
                ", username='" + username + '\'' +
                ", isActive=" + isActive +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", policeCode='" + policeCode + '\'' +
                ", fkSystemId=" + fkSystemId +
                '}';
    }
}
