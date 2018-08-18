package com.nrdc.adminPanel.jsonModel.jsonRequest;

public class RequestGetUserRoles {
    private String token;
    private Long fkUserId;

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
