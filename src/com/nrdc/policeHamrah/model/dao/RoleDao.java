package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "PH_ROLE", schema = Constants.SCHEMA)
public class RoleDao extends com.nrdc.policeHamrah.model.dto.RoleDto {
    public static final String tableName = "PH_ROLE";

    public RoleDao() {
    }

    public RoleDao(String role, Long fkSystemId, Long creatorId) {
        this.setFkSystemId(fkSystemId);
        this.setFkCreatorId(creatorId);
        this.setRole(role);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_ROLE", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "ROLE_TEXT", table = tableName)
    public String getRole() {
        return super.getRole();
    }

    @Override
    @Basic
    @Column(name = "FK_CREATOR_ID", table = tableName)
    public Long getFkCreatorId() {
        return super.getFkCreatorId();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", table = tableName)
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }
}
