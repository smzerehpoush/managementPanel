package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dao.UserRoleDAO;

import javax.persistence.*;

@Entity
@Table(name = "USER_ROLE", schema = Constants.SCHEMA)
public class UserRole extends UserRoleDAO {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_USER_ROLE")
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
