package com.nrdc.adminPanel.model;

import javax.persistence.*;

@Entity
@Table(name = "VT_ROLE_PRIVILEGE", schema = "MOBILE")
public class RolePrivilege {
    private Long id;
    private Long fkRoleId;
    private Long fkPrivilegeId;

    public RolePrivilege() {
    }

    public RolePrivilege(Long id, Long fkRoleId, Long fkPrivilegeId) {
        this.id = id;
        this.fkRoleId = fkRoleId;
        this.fkPrivilegeId = fkPrivilegeId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_VT_ROLE_PRIVILEGE")
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
