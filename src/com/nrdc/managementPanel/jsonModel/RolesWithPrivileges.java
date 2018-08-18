package com.nrdc.managementPanel.jsonModel;

import java.util.List;

public class RolesWithPrivileges {
    private VTRole role;
    private List<VTPrivilege> privileges;

    public VTRole getRole() {
        return role;
    }

    public void setRole(VTRole role) {
        this.role = role;
    }

    public List<VTPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<VTPrivilege> privileges) {
        this.privileges = privileges;
    }
}
