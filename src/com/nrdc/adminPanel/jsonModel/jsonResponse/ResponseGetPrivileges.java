package com.nrdc.adminPanel.jsonModel.jsonResponse;

import com.nrdc.adminPanel.model.VTPrivilege;

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
