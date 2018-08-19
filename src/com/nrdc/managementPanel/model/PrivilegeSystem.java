package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "PRIVILEGE_SYSTEM", schema = Constants.SCHEMA)
public class PrivilegeSystem {
    private Long id;
    private Long fkPrivilegeId;
    private Long fkSystemId;

    public PrivilegeSystem() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PRIVILEGE_SYSTEM")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FK_PRIVILEGE_ID")
    public Long getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public void setFkPrivilegeId(Long fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
    }

    @Basic
    @Column(name = "FK_SYSTEM_ID")
    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkRoleId) {
        this.fkSystemId = fkRoleId;
    }

}
