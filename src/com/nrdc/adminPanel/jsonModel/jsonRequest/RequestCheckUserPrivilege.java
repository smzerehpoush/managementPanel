package com.nrdc.adminPanel.jsonModel.jsonRequest;

public class RequestCheckUserPrivilege {
    private Long fkUserId;
    private Long fkPrivilegeId;

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

    @Override
    public String toString() {
        return "RequestCheckUserPrivilege{" +
                "fkUserId=" + fkUserId +
                ", fkPrivilegeId=" + fkPrivilegeId +
                '}';
    }
}
