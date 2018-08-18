package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestCurrentRange {
    private Long fkUserId;
    private Long fkPoliceCode;

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Long getFkPoliceCode() {
        return fkPoliceCode;
    }

    public void setFkPoliceCode(Long fkPoliceCode) {
        this.fkPoliceCode = fkPoliceCode;
    }

    @Override
    public String toString() {
        return "RequestCurrentRange{" +
                "fkUserId=" + fkUserId +
                ", fkPoliceCode=" + fkPoliceCode +
                '}';
    }
}
