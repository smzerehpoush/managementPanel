package com.nrdc.policeHamrah.jsonModel.jsonResponse;

import com.nrdc.policeHamrah.model.dto.UserDto;

public class ResponseLogin {
    private UserDto user;
    private String key;
    private String token;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
