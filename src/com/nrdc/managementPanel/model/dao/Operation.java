package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dto.OperationDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PH_OPERATION", schema = Constants.SCHEMA)
public class Operation extends OperationDTO {
    public static final String tableName = "PH_OPERATION";
    public Operation() {
    }

    public Operation(Long fkUserId, Long fkPrivilegeId) {
        this.setFkUserId(fkUserId);
        this.setFkPrivilegeId(fkPrivilegeId);
    }

    public Operation(User user, Privilege privilege, Long statusCode) {
        this.setFkUserId(user.getId());
        this.setFkPrivilegeId(privilege.getId());
        this.setStatusCode(statusCode);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_OPERATION",table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_USER_ID",table = tableName)
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Basic
    @Column(name = "FK_PRIVILEGE_ID",table = tableName)
    public Long getFkPrivilegeId() {
        return super.getFkPrivilegeId();
    }

    @Override
    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "TIME",table = tableName)
    public Date getTime() {
        return super.getTime();
    }

    @Override
    @Basic
    @Column(name = "DESCRIPTION",table = tableName)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @Basic
    @Column(name = "STATUS_CODE",table = tableName)
    public Long getStatusCode() {
        return super.getStatusCode();
    }

    @Override
    @Basic
    @Column(name = "USER_TOKEN",table = tableName)
    public String getUserToken() {
        return super.getUserToken();
    }
}
