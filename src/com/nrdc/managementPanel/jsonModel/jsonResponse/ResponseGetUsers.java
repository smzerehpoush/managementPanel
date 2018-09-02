package com.nrdc.managementPanel.jsonModel.jsonResponse;

import com.nrdc.managementPanel.model.User;

import java.util.List;

public class ResponseGetUsers {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
