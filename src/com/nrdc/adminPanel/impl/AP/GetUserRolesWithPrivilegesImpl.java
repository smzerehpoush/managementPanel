package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.exceptions.NotValidTokenException;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.RolesWithPrivileges;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetUserRolesWithPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUserRolesWithPrivileges;
import com.nrdc.adminPanel.model.VTPrivilege;
import com.nrdc.adminPanel.model.VTRole;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

public class GetUserRolesWithPrivilegesImpl {
    private static Logger logger = Logger.getLogger(GetUserRolesWithPrivilegesImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();


    public VTResponse<ResponseGetUserRolesWithPrivileges> getUserRolesWithPrivileges(RequestGetUserRolesWithPrivileges requestGetUserRolesWithPrivileges) throws NotValidTokenException {
        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
                entityManager.getEntityManagerFactory().getCache().evictAll();
            }
            String token = requestGetUserRolesWithPrivileges.getToken();
            logger.info("token : " + token);
            Database.adminTokenValidation(token);
            Long fkUserId = requestGetUserRolesWithPrivileges.getFkUserId();
            List<RolesWithPrivileges> rolesWithPrivileges = getUserRolesWithPrivileges(fkUserId);
            ResponseGetUserRolesWithPrivileges responseGetRolesWithPrivileges = new ResponseGetUserRolesWithPrivileges();
            responseGetRolesWithPrivileges.setRolesWithPrivileges(getUserRolesWithPrivileges(fkUserId));
            VTResponse<ResponseGetUserRolesWithPrivileges> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetRolesWithPrivileges);
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    private List<VTRole> getRolesList(Long fkUserId) {
        Query query = entityManager.createQuery("SELECT r FROM VTRole r JOIN VTUserRole ur ON ur.fkRoleId = r.id WHERE ur.fkUserId = :fkUserId")
                .setParameter("fkUserId", fkUserId);
        return query.getResultList();
    }

    private List<RolesWithPrivileges> getUserRolesWithPrivileges(Long fkUserId) {
        List<RolesWithPrivileges> rolesWithPrivileges = new LinkedList<>();
        List<VTRole> roles = getRolesList(fkUserId);
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
