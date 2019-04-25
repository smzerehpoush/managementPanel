package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.LogLoginDto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PH_LOG_LOGIN", schema = Constants.SCHEMA)
public class LogLoginDao extends LogLoginDto {
    public static final String tableName = "PH_LOG_LOGIN";

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", table = tableName)
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }

    @Override
    @Basic
    @Column(name = "SYSTEM_NAME", table = tableName)
    public String getSystemName() {
        return super.getSystemName();
    }

    @Override
    @Basic
    @Column(name = "FK_PROVINCE_ID", table = tableName)
    public Long getFkProvinceId() {
        return super.getFkProvinceId();
    }

    @Override
    @Id
    @GeneratedValue
    @Column(name = "ID_PH_LOG_LOGIN", table = tableName)
    public String getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_USER_ID", table = tableName)
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Basic
    @Column(name = "USERNAME", table = tableName)
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @Basic
    @Column(name = "PHONE_NUMBER", table = tableName)
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    @Basic
    @Column(name = "FIRST_NAME", table = tableName)
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    @Basic
    @Column(name = "LAST_NAME", table = tableName)
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    @Basic
    @Column(name = "NATIONAL_ID", table = tableName)
    public String getNationalId() {
        return super.getNationalId();
    }

    @Override
    @Basic
    @Column(name = "POLICE_CODE", table = tableName)
    public String getPoliceCode() {
        return super.getPoliceCode();
    }

    @Override
    @Basic
    @Column(name = "TOKEN", table = tableName)
    public String getToken() {
        return super.getToken();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Override
    @Basic
    @Column(name = "LOG_TIME", table = tableName)
    public Date getTime() {
        return super.getTime();
    }

    @Override
    @Basic
    @Column(name = "DESCRIPTION", table = tableName)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @Basic
    @Column(name = "STATUS_CODE", table = tableName)
    public Long getStatusCode() {
        return super.getStatusCode();
    }
}
