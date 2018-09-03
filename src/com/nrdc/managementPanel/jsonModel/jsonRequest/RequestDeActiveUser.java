package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestDeActiveUser {
    private Long fkUserId;
    private Long fkSystemId;

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    @Override
    public String toString() {
        return "RequestDeActiveUser{" +
                "fkUserId=" + fkUserId +
                ", fkSystemId=" + fkSystemId +
                '}';
    }
}
