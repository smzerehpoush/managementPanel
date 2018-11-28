package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAddTokenKey;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.policeHamrah.model.dao.*;
import com.nrdc.policeHamrah.model.dto.AuthDto;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;
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
        entityManager.getEntityManagerFactory().getCache().evictAll();
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

            UserDao user = verifyUser(requestLogin);
            SystemDao systemDao = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
            PrivilegeDto privilege = PrivilegeDao.getPrivilege(PrivilegeNames.LOGIN);
            operation.setFkPrivilegeId(privilege.getId());
            AuthDto auth = new AuthDao(user, systemDao);
            entityManager.persist(auth);
            ResponseLogin responseLogin = new ResponseLogin();
            responseLogin.setKey(auth.getKey());
            responseLogin.setToken(auth.getToken());
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

    public StandardResponse<ResponseLogin> loginToSystem(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();

        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
            }
            UserDao user = UserDao.getUser(token);
            user.isActive();
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            user.checkPrivilege(PrivilegeNames.LOGIN, fkSystemId);
            StandardResponse<ResponseLogin> response = loginToSystem(user, systemDao);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return response;
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(exception);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private StandardResponse<ResponseLogin> loginToSystem(UserDao user, SystemDao systemDao) throws Exception {
        user.checkKey(systemDao);
        user.checkToken(systemDao);
        AuthDao auth = new AuthDao(user, systemDao);
        StandardResponse responseAddTokenKey = sendTokenKeyToSystem(user, auth , systemDao);
        if (responseAddTokenKey.getResultCode() == -1) {
            throw new Exception(responseAddTokenKey.getResultMessage());
        }
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.persist(auth);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            StandardResponse<ResponseLogin> response = createResponseLogin(user, systemDao, auth);
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

    private StandardResponse<ResponseLogin> createResponseLogin(UserDao dbUser, SystemDao systemDao, AuthDao auth) throws Exception {
        StandardResponse<ResponseLogin> response = new StandardResponse<>();
        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setKey(auth.getKey());
        responseLogin.setToken(auth.getToken());
        UserDto user = (UserDao) dbUser.clone();
//        if (systemDao.getSystemName().equals(SystemNames.VEHICLE_TICKET.name())) {
//            user.setPassword("");
//
//        } else {
//            throw new Exception(Constants.NOT_VALID_SYSTEM);
//        }
        responseLogin.setUser(user);
        return response;
    }

    private StandardResponse sendTokenKeyToSystem(UserDao user, AuthDao auth, SystemDao systemDao) throws Exception {
        RequestAddTokenKey requestAddTokenKey = new RequestAddTokenKey(auth.getToken(), auth.getKey(), user.getId());
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
            throw new Exception("Can not add token to system : " + connection.getResponseCode());
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
        UserDao user = UserDao.getUser(username, password, phoneNumber);
        if (user.getIsActive())
            return user;
        else
            throw new Exception(Constants.NOT_ACTIVE_USER);
    }

    private String decryptPassword(String username, String encryptedPassword) throws Exception {
        String key = getUserPassword(username);
        return Encryption.decryptPassword(key, encryptedPassword);
    }

    private String getUserPassword(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
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