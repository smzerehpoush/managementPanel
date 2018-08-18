package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestAddVTUser {
    private String token;
    private VTUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public VTUser getUser() {
        return user;
    }

    public void setUser(VTUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RequestAddUser{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
