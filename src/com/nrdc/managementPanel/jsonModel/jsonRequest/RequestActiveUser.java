package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestActiveUser {
    private Long fkUserId;

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Override
    public String toString() {
        return "RequestActiveUser{" +
                "fkUserId=" + fkUserId +
                '}';
    }
}
