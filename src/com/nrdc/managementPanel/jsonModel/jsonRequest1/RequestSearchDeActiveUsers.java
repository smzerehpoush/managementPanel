package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestSearchDeActiveUsers {
    private String token;
    private String text;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "RequestSearchUsers{" +
                "token='" + token + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
