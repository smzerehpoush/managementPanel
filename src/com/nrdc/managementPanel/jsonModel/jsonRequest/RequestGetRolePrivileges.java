package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestGetRolePrivileges {
    private Long fkRoleId;

    public Long getFkRoleId() {
        return fkRoleId;
    }

    public void setFkRoleId(Long fkRoleId) {
        this.fkRoleId = fkRoleId;
    }

    @Override
    public String toString() {
        return "RequestGetRolePrivileges{" +
                "fkRoleId=" + fkRoleId +
                '}';
    }
}
