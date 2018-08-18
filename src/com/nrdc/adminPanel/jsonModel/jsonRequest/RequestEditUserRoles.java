package com.nrdc.adminPanel.jsonModel.jsonRequest;

import java.util.List;

public class RequestEditUserRoles {
    private String token;
    private Long fkUserId;
    private List<Long> fkRoleIdList;

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

    public List<Long> getFkRoleIdList() {
        return fkRoleIdList;
    }

    public void setFkRoleIdList(List<Long> fkRoleIdList) {
        this.fkRoleIdList = fkRoleIdList;
    }

    @Override
    public String toString() {
        return "RequestEditUserRoles{" +
                "token='" + token + '\'' +
                ", fkUserId=" + fkUserId +
                ", fkRoleIdList=" + fkRoleIdList +
                '}';
    }
}
