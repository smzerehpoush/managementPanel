package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestRemoveToken {
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
        return "RequestRemoveToken{" +
                "token='" + token + '\'' +
                ", fkUserId=" + fkUserId +
                '}';
    }
}
