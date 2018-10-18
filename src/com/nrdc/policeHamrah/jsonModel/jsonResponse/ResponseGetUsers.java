package com.nrdc.policeHamrah.jsonModel.jsonResponse;

import com.nrdc.policeHamrah.model.dto.UserDto;

import java.util.List;

public class ResponseGetUsers {
    private List<UserDto> users;

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
