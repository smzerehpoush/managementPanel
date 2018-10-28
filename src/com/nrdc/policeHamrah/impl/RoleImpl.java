package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAddRole;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestEditRole;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetRoles;
import com.nrdc.policeHamrah.model.dao.RoleDao;
import com.nrdc.policeHamrah.model.dao.RolePrivilegeDao;
import com.nrdc.policeHamrah.model.dao.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RoleImpl {
    public StandardResponse addRole(String token, RequestAddRole requestAddRole) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.ADD_ROLE, requestAddRole.getFkSystemId());
            RoleDao role = new RoleDao(requestAddRole.getRoleText(), requestAddRole.getFkSystemId(), user.getId());
            Long roleId = (Long) (entityManager.createQuery("SELECT MAX (r.id) FROM RoleDao r")
                    .getSingleResult())
                    + 1;
            role.setId(roleId);
            if (!transaction.isActive())
                transaction.begin();

            entityManager.persist(role);
            for (Long privilegeId : requestAddRole.getPrivileges()) {
                RolePrivilegeDao rp = new RolePrivilegeDao();
                rp.setFkRoleId(roleId);
                rp.setFkPrivilegeId(privilegeId);
                entityManager.persist(rp);
            }
            if (transaction.isActive())
                transaction.commit();
            return new StandardResponse();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse editRole(String token, RequestEditRole requestEditRole) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.EDIT_ROLE, requestEditRole.getFkSystemId());
            if (!transaction.isActive())
                transaction.begin();
            //delete all privileges of roll and add new privileges
            entityManager.createQuery("DELETE FROM RolePrivilegeDao rp WHERE rp.fkRoleId = :fkRoleId")
                    .setParameter("fkRoleId", requestEditRole.getFkRoleId())
                    .executeUpdate();

            for (Long privilegeId : requestEditRole.getPrivileges()) {
                RolePrivilegeDao rp = new RolePrivilegeDao();
                rp.setFkRoleId(requestEditRole.getFkRoleId());
                rp.setFkPrivilegeId(privilegeId);
                entityManager.persist(rp);
            }
            if (transaction.isActive())
                transaction.commit();
            return new StandardResponse();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse removeRole(String token, Long fkRoleId, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.REMOVE_ROLE, fkSystemId);
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("DELETE FROM RoleDao r WHERE r.id = :roleId")
                    .setParameter("roleId", fkRoleId)
                    .executeUpdate();
            entityManager.createQuery("DELETE FROM RolePrivilegeDao rp WHERE rp.fkRoleId = :fkRoleId")
                    .setParameter("fkRoleId", fkRoleId)
                    .executeUpdate();

            if (transaction.isActive())
                transaction.commit();
            return new StandardResponse();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getSystemRoles(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.GET_ROLES);
            List roles = entityManager.createQuery("SELECT r FROM RoleDao r")
                    .getResultList();
            ResponseGetRoles responseGetRoles = new ResponseGetRoles();
            responseGetRoles.setRoles(roles);
            StandardResponse response = new StandardResponse<>();


            response.setResponse(responseGetRoles);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getRolePrivileges(String token, Long fkRoleId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.GET_PRIVILEGES);
                    .setParameter("fkRoleId", fkRoleId)
                    .getResultList();
            ResponseGetPrivileges responseGetPrivileges = new ResponseGetPrivileges();
            responseGetPrivileges.setPrivileges(privileges);
            StandardResponse response = new StandardResponse<>();


            response.setResponse(responseGetPrivileges);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
