package com.nrdc.managementPanel.jsonModel.jsonResponse;

import java.util.List;

public class ResponseGetPrivileges {
    private List<VTPrivilege> privileges;

    public List<VTPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<VTPrivilege> privileges) {
        this.privileges = privileges;
    }
}
