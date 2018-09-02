package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.model.dao.RoleDAO;

public class Role extends RoleDAO {

    public Role() {
    }

    public Role(Long id, String role) {
        this.setId(id);
        this.setRole(role);
    }


}
