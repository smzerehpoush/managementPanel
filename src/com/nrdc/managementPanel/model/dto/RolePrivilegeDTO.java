package com.nrdc.managementPanel.model.dto;

public class RolePrivilegeDTO extends BaseModel {
    private Long id;
    private Long fkRoleId;
    private Long fkPrivilegeId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkRoleId() {
        return fkRoleId;
    }

    public void setFkRoleId(Long fkRoleId) {
        this.fkRoleId = fkRoleId;
    }

    public Long getFkPrivilegeId() {
        return fkPrivilegeId;
    }

    public void setFkPrivilegeId(Long fkPrivilegeId) {
        this.fkPrivilegeId = fkPrivilegeId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RolePrivilegeDTO{");
        sb.append("id=").append(id);
        sb.append(", fkRoleId=").append(fkRoleId);
        sb.append(", fkPrivilegeId=").append(fkPrivilegeId);
        sb.append('}');
        return sb.toString();
    }
}