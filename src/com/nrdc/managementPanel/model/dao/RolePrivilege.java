package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dto.RolePrivilegeDTO;

import javax.persistence.*;

@Entity
@Table(name = "PH_ROLE_PRIVILEGE", schema = Constants.SCHEMA)
public class RolePrivilege extends RolePrivilegeDTO {
public static final String tableName="PH_ROLE_PRIVILEGE";
    public RolePrivilege() {
    }

    public RolePrivilege(Long id, Long fkRoleId, Long fkPrivilegeId) {
        this.setId(id);
        this.setFkRoleId(fkRoleId);
        this.setFkPrivilegeId(fkPrivilegeId);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_ROLE_PRIVILEGE", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_ROLE_ID", table = tableName)
    public Long getFkRoleId() {
        return super.getFkRoleId();
    }

    @Override
    @Basic
    @Column(name = "FK_PRIVILEGE_ID", table = tableName)
    public Long getFkPrivilegeId() {
        return super.getFkPrivilegeId();
    }
}
