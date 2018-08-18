package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestRemoveRole {
    private String token;
    private Long roleId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "RequestRemoveRole{" +
                "token='" + token + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
