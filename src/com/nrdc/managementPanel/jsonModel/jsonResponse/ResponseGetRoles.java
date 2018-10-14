package com.nrdc.managementPanel.jsonModel.jsonResponse;

import com.nrdc.managementPanel.model.dao.Role;

import java.util.List;

public class ResponseGetRoles {
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
