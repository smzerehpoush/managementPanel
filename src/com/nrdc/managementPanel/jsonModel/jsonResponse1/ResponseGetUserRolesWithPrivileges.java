package com.nrdc.managementPanel.jsonModel.jsonResponse;

import com.nrdc.managementPanel.jsonModel.RolesWithPrivileges;

import java.util.List;

public class ResponseGetUserRolesWithPrivileges {
    private List<RolesWithPrivileges> rolesWithPrivileges;

    public List<RolesWithPrivileges> getRolesWithPrivileges() {
        return rolesWithPrivileges;
    }

    public void setRolesWithPrivileges(List<RolesWithPrivileges> rolesWithPrivileges) {
        this.rolesWithPrivileges = rolesWithPrivileges;
    }

}
