package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestGetSystems {
    private Long fkUserId;

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Override
    public String toString() {
        return "RequestGetSystems{" +
                "fkUserId=" + fkUserId +
                '}';
    }
}
