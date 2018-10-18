package com.nrdc.policeHamrah.jsonModel;

public class EncryptedRequest {
    private String token;

    private String data;

    public EncryptedRequest() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "EncryptedRequest{" +
                "token='" + token + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
