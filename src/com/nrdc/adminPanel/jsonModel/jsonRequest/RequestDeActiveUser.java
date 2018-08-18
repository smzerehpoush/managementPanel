package com.nrdc.adminPanel.jsonModel.jsonRequest;

public class RequestDeActiveUser {
    private String token;
    private Integer fkUserId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Integer fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Override
    public String toString() {
        return "RequestDeActiveUser{" +
                "token='" + token + '\'' +
                ", fkUserId=" + fkUserId +
                '}';
    }
}
