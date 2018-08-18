package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseAdminLogin {
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
}
