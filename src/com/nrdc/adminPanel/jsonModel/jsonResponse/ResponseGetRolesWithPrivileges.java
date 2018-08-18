package com.nrdc.adminPanel.jsonModel.jsonResponse;

import com.nrdc.adminPanel.jsonModel.RolesWithPrivileges;

import java.util.List;

public class ResponseGetRolesWithPrivileges {
    private List<RolesWithPrivileges> rolesWithPrivileges;

    public List<RolesWithPrivileges> getRolesWithPrivileges() {
        return rolesWithPrivileges;
    }

    public void setRolesWithPrivileges(List<RolesWithPrivileges> rolesWithPrivileges) {
        this.rolesWithPrivileges = rolesWithPrivileges;
    }

}
