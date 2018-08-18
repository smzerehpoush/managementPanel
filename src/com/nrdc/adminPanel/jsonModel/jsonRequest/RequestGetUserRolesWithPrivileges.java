package com.nrdc.adminPanel.jsonModel.jsonRequest;

public class RequestGetUserRolesWithPrivileges {
    private String token;
    private Long fkUserId;

    public RequestGetUserRolesWithPrivileges() {
    }

    public RequestGetUserRolesWithPrivileges(String token, Long fkUserId) {
        this.token = token;
        this.fkUserId = fkUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Override
    public String toString() {
        return "RequestGetUserRolesWithPrivileges{" +
                "token='" + token + '\'' +
                ", fkUserId=" + fkUserId +
                '}';
    }
}
