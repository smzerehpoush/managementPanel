package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PH_OPERATION", schema = Constants.SCHEMA)
public class OperationDao extends com.nrdc.policeHamrah.model.dto.OperationDto {
    public static final String tableName = "PH_OPERATION";
    public OperationDao() {
    }

    public OperationDao(Long fkUserId, Long fkPrivilegeId) {
        this.setFkUserId(fkUserId);
        this.setFkPrivilegeId(fkPrivilegeId);
    }

    public OperationDao(UserDao user, PrivilegeDao privilege, Long statusCode) {
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
