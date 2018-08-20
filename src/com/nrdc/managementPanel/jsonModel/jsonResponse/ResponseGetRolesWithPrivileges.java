package com.nrdc.managementPanel.jsonModel.jsonResponse;

import com.nrdc.managementPanel.jsonModel.customizedModel.RoleWithPrivileges;

import java.util.List;

public class ResponseGetRolesWithPrivileges {
    private List<RoleWithPrivileges> roleWithPrivileges;

    public List<RoleWithPrivileges> getRoleWithPrivileges() {
        return roleWithPrivileges;
    }

    public void setRoleWithPrivileges(List<RoleWithPrivileges> roleWithPrivileges) {
        this.roleWithPrivileges = roleWithPrivileges;
    }
}
