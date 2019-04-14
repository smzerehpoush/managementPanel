package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.LogLoginDto;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by mhd.zerehpoosh on 4/13/2019.
 */
@Entity
@Table(name = "PH_LOG_LOGIN", schema = Constants.SCHEMA)
public class LogLoginDao extends LogLoginDto {
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public String getNationalId() {
        return super.getNationalId();
    }

    @Override
    public String getPoliceCode() {
        return super.getPoliceCode();
    }

    @Override
    public String getToken() {
        return super.getToken();
    }

    @Override
    public Date getTime() {
        return super.getTime();
    }

    @Override
    public String getPrivilegeName() {
        return super.getPrivilegeName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public Long getStatusCode() {
        return super.getStatusCode();
    }
}
