package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "PH_PRIVILEGE_SYSTEM", schema = Constants.SCHEMA)
public class PrivilegeSystemDao extends com.nrdc.policeHamrah.model.dto.PrivilegeSystemDto {
    public static final String tableName = "PH_PRIVILEGE_SYSTEM";

    public PrivilegeSystemDao() {
    }

    public PrivilegeSystemDao(Long fkSystemId, Long fkPrivilegeId) {
        this.setFkSystemId(fkSystemId);
        this.setFkPrivilegeId(fkPrivilegeId);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_PRIVILEGE_SYSTEM", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", table = tableName)
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }

    @Override
    @Basic
    @Column(name = "FK_PRIVILEGE_ID", table = tableName)
    public Long getFkPrivilegeId() {
        return super.getFkPrivilegeId();
    }
}
