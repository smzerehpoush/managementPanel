package com.nrdc.adminPanel.jsonModel.jsonResponse;

import com.nrdc.adminPanel.model.VTPrivilege;

import java.util.Set;

public class ResponseGetUserPrivileges {
    private Set<VTPrivilege> privileges;

    public Set<VTPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<VTPrivilege> privileges) {
        this.privileges = privileges;
    }
}
