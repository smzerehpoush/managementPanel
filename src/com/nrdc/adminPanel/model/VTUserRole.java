package com.nrdc.adminPanel.model;

import javax.persistence.*;

@Entity
@Table(name = "VT_USER_ROLE", schema = "MOBILE")
public class VTUserRole {
    private Long id;
    private Long fkUserId;
    private Long fkRoleId;

    public VTUserRole() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_VT_USER_ROLE")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FK_USER_ID")
    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Basic
    @Column(name = "FK_ROLE_ID")
    public Long getFkRoleId() {
        return fkRoleId;
    }

    public void setFkRoleId(Long fkRoleId) {
        this.fkRoleId = fkRoleId;
    }
}
