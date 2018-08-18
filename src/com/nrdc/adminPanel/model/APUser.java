package com.nrdc.adminPanel.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AP_USER", schema = "MOBILE")
public class APUser {
    private Long id;
    private String password;
    private String username;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private String nationalId;
    private Date birthDate;
    private String phoneNumber;
    private Date registerDate;
    private String policeCode;
    private Date lastLogin;

    public APUser() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_AP_USER")
    public Long getId() {
        return id;
    }

    public void setId(Long pkUserId) {
        this.id = pkUserId;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "IS_ACTIVE")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "NATIONAL_ID")
    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTH_DATE")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Basic
    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_DATE")
    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date regDate) {
        this.registerDate = regDate;
    }

    @Basic
    @Column(name = "POLICE_CODE")
    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "VTUser{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", isActive=" + isActive +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", birthDate=" + birthDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registerDate=" + registerDate +
                ", policeCode='" + policeCode + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof APUser)) return false;

        APUser VTUser = (APUser) o;

        if (!getId().equals(VTUser.getId())) return false;
        if (!getUsername().equals(VTUser.getUsername())) return false;
        if (getNationalId() != null ? !getNationalId().equals(VTUser.getNationalId()) : VTUser.getNationalId() != null)
            return false;
        return getPoliceCode().equals(VTUser.getPoliceCode());
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
