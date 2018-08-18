package com.nrdc.adminPanel.jsonModel;

import com.nrdc.adminPanel.model.VTUser;

import java.util.Date;

public class CustomUser {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String nationalId;
    private Date birthDate;
    private String phoneNumber;
    private String policeCode;
    private Date lastLogin;

    public CustomUser() {

    }

    public CustomUser(VTUser VTUser) {
        this.id = VTUser.getId();
        this.username = VTUser.getUsername();
        this.firstName = VTUser.getFirstName();
        this.lastName = VTUser.getLastName();
        this.nationalId = VTUser.getNationalId();
        this.birthDate = VTUser.getBirthDate();
        this.phoneNumber = VTUser.getPhoneNumber();
        this.policeCode = VTUser.getPoliceCode();
        this.lastLogin = VTUser.getLastLogin();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomUser)) return false;

        CustomUser that = (CustomUser) o;

        if (!getId().equals(that.getId())) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        if (getNationalId() != null ? !getNationalId().equals(that.getNationalId()) : that.getNationalId() != null)
            return false;
        return getPoliceCode().equals(that.getPoliceCode());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + (getNationalId() != null ? getNationalId().hashCode() : 0);
        result = 31 * result + getPoliceCode().hashCode();
        return result;
    }
}
