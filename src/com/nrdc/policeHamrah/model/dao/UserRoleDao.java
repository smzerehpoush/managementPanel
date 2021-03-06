package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "PH_USER_ROLE", schema = Constants.SCHEMA)
public class UserRoleDao extends com.nrdc.policeHamrah.model.dto.UserRoleDto {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_USER_ROLE")
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
    @Column(name = "FK_ROLE_ID")
    public Long getFkRoleId() {
        return super.getFkRoleId();
    }
}
