package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.managementPanel.model.dto.*;
import com.nrdc.managementPanel.model.dto.System;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;

public class LoginImpl {
    public StandardResponse login(RequestLogin requestLogin) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Operation operation = new Operation();
        operation.setTime(new Date());
        Privilege privilege = Privilege.getPrivilege(PrivilegeNames.LOGIN);
        operation.setFkPrivilegeId(privilege.getId());
        StringBuilder stringBuilder = new StringBuilder();
        String description = stringBuilder.append("someone wants to login with Username : ")
                .append(requestLogin.getUsername())
                .append(" ,Password : ")
                .append(requestLogin.getPassword())
                .append(" ,Phone Number : ")
                .append(requestLogin.getPhoneNumber())
                .toString();
        operation.setDescription(description);
        try {
            if (!transaction.isActive())
                transaction.begin();
            User user = verifyUser(requestLogin.getUsername(), requestLogin.getPassword(), requestLogin.getPhoneNumber());
            System system = System.getSystem(SystemNames.MANAGEMENT_PANEL);
            Token token = new Token(user, system);
            entityManager.persist(token);
            Key key = new Key(user, system);
            entityManager.persist(key);
            ResponseLogin responseLogin = new ResponseLogin();
            responseLogin.setKey(key.getKey());
            responseLogin.setToken(token.getToken());
            responseLogin.setUser(user.createCustomUser());
            StandardResponse response = StandardResponse.getOKResponse();
            response.setResponse(responseLogin);
            operation.setFkUserId(user.getId());
            operation.setStatusCode(1L);
            entityManager.persist(operation);
            if (transaction.isActive())
                transaction.commit();
            return response;
        } catch (Exception ex) {
            operation.setStatusCode(-1L);
            entityManager.persist(operation);
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private User verifyUser(String username, String encryptedPassword, String phoneNumber) throws Exception {

        String password = decryptPassword(username, encryptedPassword);
        return User.getUser(username, password, phoneNumber);
    }

    private String decryptPassword(String username, String encryptedPassword) throws Exception {
        String key = getUserPassword(username);
        return Encryption.decryptPassword(key, encryptedPassword);
    }

    private String getUserPassword(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (String) entityManager.createQuery("SELECT u.password FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (Exception ex) {
            throw new Exception(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }
}