package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.model.dao.OperationDAO;

public class Operation extends OperationDAO {

    public Operation() {
    }

    public Operation(Long fkUserId, Long fkPrivilegeId) {
        this.setFkUserId(fkUserId);
        this.setFkPrivilegeId(fkPrivilegeId);
    }

    public Operation(User user, Privilege privilege, Long statusCode) {
        this.setFkUserId(user.getId());
        this.setFkPrivilegeId(privilege.getId());
        this.setStatusCode(statusCode);
    }


}
