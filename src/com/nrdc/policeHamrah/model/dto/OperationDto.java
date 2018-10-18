package com.nrdc.policeHamrah.model.dto;

import java.util.Date;

public abstract class OperationDto implements BaseModel {
    private Long id;
    private Long fkUserId;
    private Long fkPrivilegeId;
    private Date time;
    private String description;
    private Long statusCode;
    private String userToken;

    public OperationDto(){
        this.time = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Long getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public void setFkPrivilegeId(Long fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OperationDao{");
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
