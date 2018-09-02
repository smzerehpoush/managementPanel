package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.model.dao.RolePrivilegeDAO;

public class RolePrivilege extends RolePrivilegeDAO {

    public RolePrivilege() {
    }

    public RolePrivilege(Long id, Long fkRoleId, Long fkPrivilegeId) {
        this.setId(id);
        this.setFkRoleId(fkRoleId);
        this.setFkPrivilegeId(fkPrivilegeId);
    }
}
