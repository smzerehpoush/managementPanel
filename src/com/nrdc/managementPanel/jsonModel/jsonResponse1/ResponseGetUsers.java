package com.nrdc.managementPanel.jsonModel.jsonResponse;

import java.util.List;

public class ResponseGetUsers {
    private List<CustomUser> users;

    public List<CustomUser> getUsers() {
        return users;
    }

    public void setUsers(List<CustomUser> users) {
        this.users = users;
    }
}
