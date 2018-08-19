package com.nrdc.managementPanel.impl;

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
    public StandardResponse activeUser (String token, RequestActiveUser request) throws Exception {
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

    public StandardResponse deActiveUser (String token, RequestDeActiveUser request) throws Exception {
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
            System system = System.getSystem(requestAddUser.getFkSystemId());
            String privilegeName = "ADD_"+system.getSystemName()+"_USER";
            user.checkPrivilege(privilegeName);
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

    public StandardResponse getUsers(String token, RequestGetUsers requestAddUser) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            System system = System.getSystem(requestAddUser.getFkSystemId());
            String privilegeName = "GET_"+system.getSystemName()+"_USERS";
            user.checkPrivilege(privilegeName);
            List<User> users = entityManager.createQuery("SELECT u FROM User u JOIN UserSystem us ON u.id = us.fkUserId WHERE us.fkSystemId = :fkSystemId")
                    .setParameter("fkSystemId",system.getId())
                    .getResultList();
            for (User u : users){
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
