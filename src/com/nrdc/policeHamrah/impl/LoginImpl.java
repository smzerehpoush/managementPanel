package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAddTokenKey;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLoginToSystems;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.policeHamrah.model.dao.*;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;
import com.nrdc.policeHamrah.model.dto.TokenDto;
import com.nrdc.policeHamrah.model.dto.UserDto;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginImpl {
    private static Logger logger = Logger.getLogger(LoginImpl.class.getName());

    /**
     * @param requestLogin username, phoneNumber, password to login into Police Hamrah
     * @return StandardResponse with value of {@link ResponseLogin}
     */
    public StandardResponse login(RequestLogin requestLogin) {
        EntityManager entityManager = Database.getEntityManager();
        EntityManager operationEntityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        EntityTransaction operationTransaction = operationEntityManager.getTransaction();
        OperationDao operation = new OperationDao();
        operation.setPrivilegeName(PrivilegeNames.LOGIN.name());
        String description = new StringBuilder().append("someone wants to login with Username : ")
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
            if (!operationTransaction.isActive())
                operationTransaction.begin();

            PrivilegeDto privilege = PrivilegeDao.getPrivilege(PrivilegeNames.LOGIN);
            operation.setFkPrivilegeId(privilege.getId());
            UserDao user = verifyUser(requestLogin);
            SystemDao systemDao = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
            TokenDto token = new TokenDao(user, systemDao);
            entityManager.persist(token);
            KeyDao key = new KeyDao(user, systemDao);
            entityManager.persist(key);
            ResponseLogin responseLogin = new ResponseLogin();
            responseLogin.setKey(key.getKey());
            responseLogin.setToken(token.getToken());
            responseLogin.setUser(user.createCustomUser());
            StandardResponse response = new StandardResponse();
            response.setResponse(responseLogin);
            operation.setFkUserId(user.getId());
            operation.setStatusCode(1L);
            if (transaction.isActive())
                transaction.commit();
            return response;
        } catch (Exception ex) {
            operation.setStatusCode(-1L);
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            operationEntityManager.persist(operation);
            if (operationTransaction.isActive())
                operationTransaction.commit();
            if (entityManager.isOpen())
                entityManager.close();
            if (operationEntityManager.isOpen())
                operationEntityManager.close();
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
            UserDao user = UserDao.getUser(token);
            SystemDao systemDao = SystemDao.getSystem(requestLogin.getFkSystemId());
            StandardResponse response = login(user, systemDao);
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

    private StandardResponse login(UserDao user, SystemDao systemDao) throws Exception {
        user.checkSystemAccess(systemDao.getId());
        user.checkKey(systemDao);
        user.checkToken(systemDao);
        KeyDao key = new KeyDao(user, systemDao);
        TokenDao token = new TokenDao(user, systemDao);
        StandardResponse responseAddTokenKey = sendTokenKeyToSystem(user, token, key, systemDao);
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
            StandardResponse response = createResponseLogin(user, systemDao, key, token);
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

    private StandardResponse<ResponseLogin> createResponseLogin(UserDao dbUser, SystemDao systemDao, KeyDao key, TokenDao token) throws Exception {
        StandardResponse<ResponseLogin> response = new StandardResponse<>();
        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setKey(key.getKey());
        responseLogin.setToken(token.getToken());
        UserDto user = (UserDao) dbUser.clone();
        if (systemDao.getSystemName().equals(SystemNames.VEHICLE_TICKET.name())) {
            user.setPassword("");
            responseLogin.setUser(user);

        } else {
            throw new Exception(Constants.NOT_VALID_SYSTEM);
        }
        return response;
    }

    public StandardResponse sendTokenKeyToSystem(UserDao user, TokenDao token, KeyDao key, SystemDao systemDao) throws Exception {
        RequestAddTokenKey requestAddTokenKey = new RequestAddTokenKey(token.getToken(), key.getKey(), user.getId());
        URL url = new URL(systemDao.getSystemPath() + "/addTokenKey");
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

    private UserDao verifyUser(RequestLogin requestLogin) throws Exception {
        String username = requestLogin.getUsername();
        String encryptedPassword = requestLogin.getPassword();
        String phoneNumber = requestLogin.getPhoneNumber();
        String password = decryptPassword(username, encryptedPassword);
        return UserDao.getUser(username, password, phoneNumber);
    }

    private String decryptPassword(String username, String encryptedPassword) throws Exception {
        String key = getUserPassword(username);
        return Encryption.decryptPassword(key, encryptedPassword);
    }

    private String getUserPassword(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (String) entityManager.createQuery("SELECT u.password FROM UserDao u WHERE u.username = :username")
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