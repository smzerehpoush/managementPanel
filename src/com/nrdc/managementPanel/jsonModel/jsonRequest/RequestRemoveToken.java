package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestRemoveToken {
    private String token;
    private Long fkSystemId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    @Override
    public String toString() {
        return "RequestRemoveToken{" +
                "token='" + token + '\'' +
                ", fkSystemId=" + fkSystemId +
                '}';
    }
}
