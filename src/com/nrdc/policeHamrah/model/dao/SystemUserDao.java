package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.SystemUserDto;

import javax.persistence.*;

@Entity
@Table(name = "PH_USER_SYSTEM", schema = Constants.SCHEMA)
public class SystemUserDao extends SystemUserDto {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_USER_SYSTEM")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_USER_ID")
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID")
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }
}
