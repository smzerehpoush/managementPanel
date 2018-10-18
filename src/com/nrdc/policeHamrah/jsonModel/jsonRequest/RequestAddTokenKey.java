package com.nrdc.policeHamrah.jsonModel.jsonRequest;

public class RequestAddTokenKey {
    private String token;
    private String key;
    private Long fkUserId;

    public RequestAddTokenKey() {
    }

    public RequestAddTokenKey(String token, String key, Long fkUserId) {
        this.token = token;
        this.key = key;
        this.fkUserId = fkUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }
}
