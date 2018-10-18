package com.nrdc.policeHamrah.jsonModel.jsonRequest;

public class RequestGetUserRolesWithPrivileges {
    private Long fkUserId;

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Override
    public String toString() {
        return "RequestGetUserRolesWithPrivileges{" +
                "fkUserId=" + fkUserId +
                '}';
    }
}
