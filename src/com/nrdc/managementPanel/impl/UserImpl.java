package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.*;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.managementPanel.model.System;
import com.nrdc.managementPanel.model.Token;
import com.nrdc.managementPanel.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class UserImpl {
    public StandardResponse activeUser(String token, RequestActiveUser request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.ACTIVATE_USER);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.isActive = true WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", request.getFkUserId())
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();

            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            if (entityManager.isOpen())
                entityManager.close();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse deActiveUser(String token, RequestDeActiveUser request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.ACTIVATE_USER);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.isActive = false WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", request.getFkUserId())
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();

            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            if (entityManager.isOpen())
                entityManager.close();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse addUser(String token, RequestAddUser requestAddUser) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.ADD_USERS);
            if (!transaction.isActive())
                transaction.begin();
            User u = new User(requestAddUser);
            entityManager.persist(u);
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
    public List<System> getUserSystems(User user){
        EntityManager entityManager = Database.getEntityManager();
        try {
            return entityManager.createQuery("SELECT s FROM System s JOIN SystemUser us ON us.fkSystemId = s.id WHERE us.fkUserId = :fkUserId")
                    .setParameter("fkUserId",user.getId())
                    .getResultList();
        }finally {
            if (entityManager!=null && entityManager.isOpen())
                entityManager.close();
        }
    }
    public StandardResponse editUser(String token, RequestEditUser requestEditUser) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            int size = entityManager.createQuery("SELECT ur1 FROM UserRole ur1 JOIN RolePrivilege rp1 ON ur1.fkRoleId = rp1.fkRoleId JOIN RolePrivilege rp2 ON rp1.fkPrivilegeId= rp2.fkPrivilegeId JOIN UserRole ur2 ON rp2.fkRoleId =ur2.fkRoleId WHERE ur1.fkUserId = :fkUserId1 AND ur2.fkUserId = :fkUserId2")
                    .setParameter("fkUserId1",user.getId())
                    .setParameter("fkUserId2",requestEditUser.getId())
                    .getResultList()
                    .size();
            if (size < 1){
                throw new Exception(Constants.PERMISSION_ERROR);
            }

            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.username = :username , u.phoneNumber= :phoneNumber , u.phoneNumber = :phoneNumber , u.firstName = :firstName , u.lastName = :lastName , u.nationalId = :nationalId , u.policeCode = :policeCode WHERE u.id = :id")
                    .setParameter("username",requestEditUser.getUsername())
                    .setParameter("phoneNumber",requestEditUser.getPhoneNumber())
                    .setParameter("firstName",requestEditUser.getFirstName())
                    .setParameter("lastName",requestEditUser.getLastName())
                    .setParameter("nationalId",requestEditUser.getNationalId())
                    .setParameter("policeCode",requestEditUser.getPoliceCode())
                    .setParameter("id",requestEditUser.getId())
                    .executeUpdate();
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getUsers(String token, RequestGetUsers requestAddUser) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            System system = System.getSystem(requestAddUser.getFkSystemId());
            String privilegeName = "GET_" + system.getSystemName() + "_USERS";
            user.checkPrivilege(privilegeName);
            List<User> users = entityManager.createQuery("SELECT u FROM User u JOIN SystemUser us ON u.id = us.fkUserId WHERE us.fkSystemId = :fkSystemId")
                    .setParameter("fkSystemId", system.getId())
                    .getResultList();
            for (User u : users) {
                u.setPassword(null);
            }
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(users);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetUsers);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse filterUsers(String token, RequestFilterUsers requestFilterUsers) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.FILTER_USERS);
            StringBuilder query = new StringBuilder();
            query.append("SELECT u FROM User u ");
            if (requestFilterUsers.getFkSystemId() != null) {
                query.append(" JOIN SystemUser us ON u.id = us.fkUserId ");
            }
            query.append(" WHERE 1=1 ");
            if (requestFilterUsers.getFkSystemId() != null) {
                query.append(" AND us.fkSystemId = ");
                query.append(requestFilterUsers.getFkSystemId());
            }
            if (requestFilterUsers.getUsername() != null) {
                query.append(" AND u.username LIKE '");
                query.append(requestFilterUsers.getUsername());
                query.append("%' ");
            }
            if (requestFilterUsers.getActive() != null) {
                query.append(" AND u.isActive = ");
                query.append(requestFilterUsers.getActive());
            }
            if (requestFilterUsers.getPhoneNumber() != null) {
                query.append(" AND u.phoneNumber LIKE '");
                query.append(requestFilterUsers.getPhoneNumber());
                query.append("%' ");
            }
            if (requestFilterUsers.getFirstName() != null) {
                query.append(" AND u.firstName LIKE '");
                query.append(requestFilterUsers.getFirstName());
                query.append("%' ");
            }
            if (requestFilterUsers.getLastName() != null) {
                query.append(" AND u.lastName LIKE '");
                query.append(requestFilterUsers.getLastName());
                query.append("%' ");
            }
            if (requestFilterUsers.getNationalId() != null) {
                query.append(" AND u.nationalId LIKE '");
                query.append(requestFilterUsers.getNationalId());
                query.append("%' ");
            }
            if (requestFilterUsers.getPoliceCode() != null) {
                query.append(" AND u.policeCode LIKE '");
                query.append(requestFilterUsers.getPoliceCode());
                query.append("%' ");
            }


            List<User> users = entityManager.createQuery(query.toString())
                    .getResultList();
            for (User u : users) {
                u.setPassword(null);
            }
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(users);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetUsers);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
