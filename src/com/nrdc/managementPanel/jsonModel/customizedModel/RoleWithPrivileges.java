package com.nrdc.managementPanel.jsonModel.customizedModel;

import com.nrdc.managementPanel.model.dao.Privilege;

import java.util.List;

public class RoleWithPrivileges {
    private Long id;
    private String role;
    private List<Privilege> privileges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}
