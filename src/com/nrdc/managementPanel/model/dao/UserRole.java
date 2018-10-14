package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dto.UserRoleDTO;

import javax.persistence.*;

@Entity
@Table(name = "PH_USER_ROLE", schema = Constants.SCHEMA)
public class UserRole extends UserRoleDTO {
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
