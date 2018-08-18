package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestAddRole;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAddRole;
import com.nrdc.adminPanel.model.RolePrivilege;
import com.nrdc.adminPanel.model.VTRole;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class AddRoleImpl {
    private static Logger logger = Logger.getLogger(AddRoleImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseAddRole> addRole(RequestAddRole requestAddRole) throws Exception {
        String token = requestAddRole.getToken();
        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
                entityManager.getEntityManagerFactory().getCache().evictAll();
            }
            logger.info(requestAddRole);
            Database.adminTokenValidation(token);
            checkRole(requestAddRole.getRole());
            VTRole role = new VTRole();
            role.setId(getId());
            role.setRole(requestAddRole.getRole());
            entityManager.persist(role);
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
                //first we should persist this to can persist role privilege
                transaction = entityManager.getTransaction();
                transaction.begin();
            }

            Long roleId = role.getId();
            for (Long privilegeId : requestAddRole.getFkPrivilegeIdList()) {

                RolePrivilege rolePrivilege = new RolePrivilege();
                rolePrivilege.setFkPrivilegeId(privilegeId);
                rolePrivilege.setFkRoleId(roleId);
                entityManager.persist(rolePrivilege);
            }

            VTResponse<ResponseAddRole> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            if (transaction != null && transaction.isActive())
                transaction.commit();
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

    private Long getId() {
        return (Long) entityManager.createQuery("select max (r.id) FROM VTRole r").getSingleResult() + 1;
    }

    private void checkRole(String roleText) throws Exception {
        Long size = (Long) entityManager.createQuery("SELECT count (r) FROM  VTRole r WHERE r.role = :roleText")
                .setParameter("roleText", roleText)
                .getSingleResult();
        if (size > 0)
            throw new Exception(Constants.NOT_UNIQUE_ROLE);
    }

}