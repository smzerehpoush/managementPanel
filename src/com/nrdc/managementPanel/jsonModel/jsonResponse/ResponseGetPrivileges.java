package com.nrdc.managementPanel.jsonModel.jsonResponse;

import com.nrdc.managementPanel.model.Privilege;

import java.util.List;

public class ResponseGetPrivileges {
    private List<Privilege> privileges;

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}