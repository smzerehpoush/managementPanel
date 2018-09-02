package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "ROLE_PRIVILEGE", schema = Constants.SCHEMA)
public class RolePrivilegeDAO extends BaseModel{
    private Long id;
    private Long fkRoleId;
    private Long fkPrivilegeId;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ROLE_PRIVILEGE")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FK_ROLE_ID")
    public Long getFkRoleId() {
        return fkRoleId;
    }

    public void setFkRoleId(Long fkRoleId) {
        this.fkRoleId = fkRoleId;
    }

    @Basic
    @Column(name = "FK_PRIVILEGE_ID")
    public Long getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public void setFkPrivilegeId(Long fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
    }
}
