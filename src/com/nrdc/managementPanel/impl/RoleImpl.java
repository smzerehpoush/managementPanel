package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestGetRolePrivileges;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseGetRoles;
import com.nrdc.managementPanel.model.dao.User;

import javax.persistence.EntityManager;
import java.util.List;

public class RoleImpl {
    public StandardResponse getRoles(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.GET_ROLES);
            List roles = entityManager.createQuery("SELECT r FROM Role r")
                    .getResultList();
            ResponseGetRoles responseGetRoles = new ResponseGetRoles();
            responseGetRoles.setRoles(roles);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetRoles);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getPrivileges(String token, RequestGetRolePrivileges requestGetRolePrivileges) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.GET_PRIVILEGES);
            List privileges = entityManager.createQuery("SELECT p FROM Privilege p JOIN RolePrivilege rp ON p.id = rp.fkPrivilegeId WHERE rp.fkRoleId = :fkRoleId")
                    .setParameter("fkRoleId", requestGetRolePrivileges.getFkRoleId())
                    .getResultList();
            ResponseGetPrivileges responseGetPrivileges = new ResponseGetPrivileges();
            responseGetPrivileges.setPrivileges(privileges);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetPrivileges);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
