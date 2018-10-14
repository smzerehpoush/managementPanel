package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dto.RoleDTO;

import javax.persistence.*;

@Entity
@Table(name = "ROLE", schema = Constants.SCHEMA)
public class Role extends RoleDTO {

    public Role() {
    }

    public Role(Long id, String role) {
        this.setId(id);
        this.setRole(role);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ROLE")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "ROLE")
    public String getRole() {
        return super.getRole();
    }

    @Override
    @Basic
    @Column(name = "FK_CREATOR_ID")
    public Long getFkCreatorId() {
        return super.getFkCreatorId();
    }
}
