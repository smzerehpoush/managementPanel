package com.nrdc.managementPanel.model.dto;

public class UserDTO extends BaseModel {

    private Long id;
    private String password;
    private String username;
    private Boolean isActive;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String nationalId;
    private String policeCode;

    public Long getId() {
        return id;
    }

    public void setId(Long pkUserId) {
        this.id = pkUserId;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        final StringBuffer sb = new StringBuffer("UserDTO{");
        sb.append("id=").append(id);
        sb.append(", password='").append(password).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", isActive=").append(isActive);
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", nationalId='").append(nationalId).append('\'');
        sb.append(", policeCode='").append(policeCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}



