package com.nrdc.adminPanel.jsonModel.jsonRequest;

import java.util.List;

public class RequestAddRole {
    private String token;
    private String role;
    private List<Long> fkPrivilegeIdList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Long> getFkPrivilegeIdList() {
        return fkPrivilegeIdList;
    }

    public void setFkPrivilegeIdList(List<Long> fkPrivilegeIdList) {
        this.fkPrivilegeIdList = fkPrivilegeIdList;
    }

    @Override
    public String toString() {
        return "RequestAddRole{" +
                "token='" + token + '\'' +
                ", role='" + role + '\'' +
                ", fkPrivilegeIdList=" + fkPrivilegeIdList +
                '}';
    }
}
