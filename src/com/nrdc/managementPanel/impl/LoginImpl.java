package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestAddTokenKey;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestLoginToSystems;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.managementPanel.model.dao.*;
import com.nrdc.managementPanel.model.dao.System;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginImpl {

    public StandardResponse login(RequestLogin requestLogin) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Operation operation = new Operation();
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
            System system = System.getSystem(SystemNames.POLICE_HAMRAH);
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

    public StandardResponse loginToSystem(String token, RequestLoginToSystems requestLogin) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
                entityManager.getEntityManagerFactory().getCache().evictAll();
            }
            User user = User.getUser(token);
            System system = System.getSystem(requestLogin.getFkSystemId());
            StandardResponse response = login(user, system);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return response;
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new StandardResponse<ResponseLogin>().getNOKExceptions(exception);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    private StandardResponse login(User user, System system) throws Exception {
        user.checkSystemAccess(system.getId());
        user.checkKey(system);
        user.checkToken(system);
        Key key = new Key(user, system);
        Token token = new Token(user, system);
        StandardResponse responseAddTokenKey = sendTokenKeyToSystem(user, token, key, system);
        if (responseAddTokenKey.getResultCode() == -1) {
            throw new Exception(responseAddTokenKey.getResultMessage());
        }
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.persist(key);
            entityManager.persist(token);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            StandardResponse response = createResponseLogin(user, system, key, token);
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;

        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private StandardResponse<ResponseLogin> createResponseLogin(User dbUser, System system, Key key, Token token) throws Exception {
        StandardResponse<ResponseLogin> response = new StandardResponse<>();
        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setKey(key.getKey());
        responseLogin.setToken(token.getToken());
        User user = (User) dbUser.clone();
        if (system.getSystemName().equals(SystemNames.VEHICLE_TICKET.name())) {
            user.setPassword("");
            responseLogin.setUser(user);

        }
        else {
            throw new Exception(Constants.NOT_VALID_SYSTEM);
        }
        return response;
    }

    public StandardResponse sendTokenKeyToSystem(User user, Token token, Key key, System system) throws Exception {
        RequestAddTokenKey requestAddTokenKey = new RequestAddTokenKey(token.getToken(), key.getKey(), user.getId());
        URL url = new URL(system.getSystemPath() + "/addTokenKey");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        String input = new ObjectMapper().writeValueAsString(requestAddTokenKey);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(input.getBytes());
        outputStream.flush();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException(Constants.CAN_NOT_RETRIEVE_DATA_FROM_NAJA + " : " + connection.getResponseCode());
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder output = new StringBuilder();
        int c;
        while ((c = bufferedReader.read()) != -1)
            output.append((char) c);
        return new ObjectMapper().readValue(output.toString(), StandardResponse.class);


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