package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.exceptions.NotValidTokenException;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.RolesWithPrivileges;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetRolesWithPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetRolesWithPrivileges;
import com.nrdc.adminPanel.model.VTPrivilege;
import com.nrdc.adminPanel.model.VTRole;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

public class GetRolesWithPrivilegesImpl {
    private static Logger logger = Logger.getLogger(GetRolesWithPrivilegesImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();


    public VTResponse<ResponseGetRolesWithPrivileges> getRolesWithPrivileges(RequestGetRolesWithPrivileges requestGetRolesWithPrivileges) throws NotValidTokenException {
        try {
            String token = requestGetRolesWithPrivileges.getToken();
            logger.info("token : " + token);
            Database.adminTokenValidation(token);
            List<RolesWithPrivileges> rolesWithPrivileges = getRolesWithPrivileges();
            ResponseGetRolesWithPrivileges responseGetRolesWithPrivileges = new ResponseGetRolesWithPrivileges();
            responseGetRolesWithPrivileges.setRolesWithPrivileges(getRolesWithPrivileges());
            VTResponse<ResponseGetRolesWithPrivileges> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetRolesWithPrivileges);
            return response;
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    private List<VTRole> getRolesList() {
        Query query = entityManager.createQuery("SELECT r FROM VTRole r");
        return query.getResultList();
    }

    private List<RolesWithPrivileges> getRolesWithPrivileges() {
        List<RolesWithPrivileges> rolesWithPrivileges = new LinkedList<>();
        List<VTRole> roles = getRolesList();
        Query query;
        RolesWithPrivileges roleWithPrivileges;
        for (VTRole role : roles) {
            roleWithPrivileges = new RolesWithPrivileges();
            roleWithPrivileges.setRole(role);
            List<Long> rolePrivilegeIdsList = entityManager.createQuery("SELECT rp.fkPrivilegeId FROM RolePrivilege rp WHERE rp.fkRoleId = :roleId")
                    .setParameter("roleId", role.getId())
                    .getResultList();
            List<VTPrivilege> privileges = new LinkedList<>();
            for (Long privilegeId : rolePrivilegeIdsList) {
                VTPrivilege privilege = (VTPrivilege) entityManager.createQuery("SELECT p FROM VTPrivilege p WHERE p.id = :pId")
                        .setParameter("pId", privilegeId)
                        .getSingleResult();
                privileges.add(privilege);
            }
            roleWithPrivileges.setPrivileges(privileges);
            rolesWithPrivileges.add(roleWithPrivileges);
        }
        return rolesWithPrivileges;
    }
}
