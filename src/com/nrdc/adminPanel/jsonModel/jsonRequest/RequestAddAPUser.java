package com.nrdc.adminPanel.jsonModel.jsonRequest;

import com.nrdc.adminPanel.model.APUser;

public class RequestAddAPUser {
    private String token;
    private APUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public APUser getUser() {
        return user;
    }

    public void setUser(APUser user) {
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
