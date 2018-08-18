package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class TokenRequest {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenRequest{" +
                "token='" + token + '\'' +
                '}';
    }
}
