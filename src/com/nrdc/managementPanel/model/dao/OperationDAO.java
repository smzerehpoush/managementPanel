package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "OPERATION", schema = Constants.SCHEMA)
public class OperationDAO extends BaseModel {
    private Long id;
    private Long fkUserId;
    private Long fkPrivilegeId;
    private Date time;
    private String description;
    private Long statusCode;
    private String userToken;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_OPERATION")
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
    @Column(name = "FK_PRIVILEGE_ID")
    public Long getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public void setFkPrivilegeId(Long fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "TIME")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "STATUS_CODE")
    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    @Basic
    @Column(name = "USER_TOKEN")
    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OperationDAO{");
        sb.append("id=").append(id);
        sb.append(", fkUserId=").append(fkUserId);
        sb.append(", fkPrivilegeId=").append(fkPrivilegeId);
        sb.append(", time=").append(time);
        sb.append(", description='").append(description).append('\'');
        sb.append(", statusCode=").append(statusCode);
        sb.append(", userToken='").append(userToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
