package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.helper.Constants;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_ROLE", schema = Constants.SCHEMA)
public class UserRole implements Serializable {
    private Long id;
    private Long fkUserId;
    private Long fkRoleId;

    public UserRole() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_USER_ROLE")
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

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", fkUserId=" + fkUserId +
                ", fkRoleId" + fkRoleId +
                '}';
    }
}