package com.nrdc.policeHamrah.impl;

import com.google.gson.Gson;
import com.nrdc.policeHamrah.helper.*;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAuthenticateUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.policeHamrah.model.dao.*;
import com.nrdc.policeHamrah.model.dto.AuthDto;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;
import com.nrdc.policeHamrah.model.dto.UserDto;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class LoginImpl {
    private static Logger logger = Logger.getLogger(LoginImpl.class.getName());

    private StandardResponse authenticateVTUser(String policeCode, String password) {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        StandardResponse response = new StandardResponse();
        try {
            try {
                UserDao user = (UserDao) entityManager.createQuery("SELECT u FROM UserDao u WHERE u.policeCode = :policeCode")
                        .setParameter("policeCode", policeCode)
                        .getSingleResult();
                if (!user.getPassword().equals(password)) {
                    response.setResultCode(-1);
                    response.setResultMessage(Constants.INCORRECT_USERNAME_OR_PASSWORD);
                }
            } catch (NoResultException ex) {
                response.setResultCode(-1);
                response.setResultMessage(Constants.UNKNOWN_USER);

            } catch (Exception ex) {
                response.setResultCode(-1);
                response.setResultMessage(Constants.NOT_VALID_USER);
            }
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
            return response;

        }
    }

    private void checkUserInPh(RequestAuthenticateUser requestAuthenticateUser) throws Exception {
        SystemDao systemDao = SystemDao.getSystem(requestAuthenticateUser.getFkSystemId());
        Long size = 0L;
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            if (systemDao.getSystemName().equals(SystemNames.NAZER.name())) {
                size = (Long) entityManager.createQuery("SELECT COUNT (u) FROM UserDao u WHERE u.phoneNumber = :phoneNumber ")
                        .setParameter("phoneNumber", requestAuthenticateUser.getPhoneNumber())
                        .getSingleResult();

            } else if (systemDao.getSystemName().equals(SystemNames.VEHICLE_TICKET.name()) || systemDao.getSystemName().equals(SystemNames.VT_REPORT.name())) {
                size = (Long) entityManager.createQuery("SELECT COUNT (u) FROM UserDao u WHERE u.policeCode = :policeCode ")
                        .setParameter("policeCode", requestAuthenticateUser.getPoliceCode())
                        .getSingleResult();
            }
            if (size.compareTo(0L) > 0)
                throw new Exception(Constants.YOU_HAVE_PH_USER);

        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse authenticateUser(RequestAuthenticateUser requestAuthenticateUser) throws Exception {
        checkUserInPh(requestAuthenticateUser);
        SystemDao systemDao = SystemDao.getSystem(requestAuthenticateUser.getFkSystemId());
        Object request = fillRequestBySystemId(systemDao.getSystemName(), requestAuthenticateUser);
        String output = CallWebService.callPostService(systemDao.getSystemPath() + "/authenticateUser", request);
        StandardResponse response = new Gson().fromJson(output, StandardResponse.class);
        if (response.getResultCode() == -1) {
            if (response.getResultMessage().trim().equals("1"))
                response.setResultMessage(Constants.UNKNOWN_USER);
            else if (response.getResultMessage().trim().equals("2"))
                response.setResultMessage(Constants.INCORRECT_USERNAME_OR_PASSWORD);
            else if (response.getResultMessage().trim().equals("3"))
                response.setResultMessage(Constants.NAZER_NOT_APN_USER);
        }
        return response;
    }

    private Object fillRequestBySystemId(String systemName, RequestAuthenticateUser requestAuthenticateUser) throws Exception {
        if (systemName.equals(SystemNames.NAZER.name())) {
            RequestAuthenticateNazer requestAuthenticateNazer = new RequestAuthenticateNazer();
            requestAuthenticateNazer.setPhoneNumber(requestAuthenticateUser.getPhoneNumber());
            requestAuthenticateNazer.setPassword(requestAuthenticateUser.getPassword());
            return requestAuthenticateNazer;
        } else if (systemName.equals(SystemNames.VEHICLE_TICKET.name())) {
            RequestAuthenticateVT requestAuthenticateVT = new RequestAuthenticateVT();
            requestAuthenticateVT.setPoliceCode(requestAuthenticateUser.getPoliceCode());
            requestAuthenticateVT.setPassword(requestAuthenticateUser.getPassword());
            return requestAuthenticateVT;
        }
        throw new Exception(Constants.NOT_VALID_SYSTEM);
    }

    private class RequestAuthenticateVT {
        private String policeCode;
        private String password;

        public String getPoliceCode() {
            return policeCode;
        }

        public void setPoliceCode(String policeCode) {
            this.policeCode = policeCode;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private class RequestAuthenticateNazer {
        private String phoneNumber;
        private String password;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("RequestAuthenticateNazer{");
            sb.append("phoneNumber='").append(phoneNumber).append('\'');
            sb.append(", password='").append(password).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

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
            SystemDao system = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
            Long size = (Long) entityManager.createQuery("SELECT COUNT (a) FROM AuthDao a JOIN UserDao u ON u.id = a.fkUserId WHERE a.fkSystemId = :fkSystemId AND (u.phoneNumber = :phoneNumber OR u.username = :username )").setParameter("phoneNumber", requestLogin.getPhoneNumber())
                    .setParameter("fkSystemId", system.getId())
                    .setParameter("phoneNumber", requestLogin.getPhoneNumber())
                    .setParameter("username", requestLogin.getUsername())
                    .getSingleResult();
            if (!size.equals(0L))
                throw new Exception(Constants.ACTIVE_USER_EXISTS);
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
            StandardResponse<ResponseLogin> response = new StandardResponse<>();
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

    /***
     * this service calls by other servers like nazer, agahi,...
     * @param policeHamrahToken token of user in policeHamrah system
     * @param fkSystemId id of system that user wants to login to it
     * @return ResponseLogin
     * @throws Exception
     */
    public StandardResponse<ResponseLogin> loginToSystem(String policeHamrahToken, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();

        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
            }
            UserDao user = UserDao.validate(policeHamrahToken);
            if (!user.getIsActive())
                throw new Exception(Constants.USER_IS_NOT_ACTIVE);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
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
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.persist(auth);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return createResponseLogin(user, systemDao, auth);

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
        response.setResponse(responseLogin);
        return response;
    }

//    private StandardResponse sendTokenKeyToSystem(UserDao user, AuthDao auth, SystemDao systemDao) throws Exception {
//        RequestAddTokenKey requestAddTokenKey = new RequestAddTokenKey();
//        requestAddTokenKey.setKey(auth.getKey());
//        requestAddTokenKey.setToken(auth.getToken());
//        requestAddTokenKey.setNationalId(user.getNationalId());
//        requestAddTokenKey.setPoliceCode(user.getPoliceCode());
//        requestAddTokenKey.setPhoneNumber(user.getPhoneNumber());
//        URL url = new URL(systemDao.getSystemPath() + "/addTokenKey");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoOutput(true);
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        String input = new ObjectMapper().writeValueAsString(requestAddTokenKey);
//        OutputStream outputStream = connection.getOutputStream();
//        outputStream.write(input.getBytes());
//        outputStream.flush();
//        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//            throw new Exception("Can not add token to system : " + connection.getResponseCode());
//        }
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuilder output = new StringBuilder();
//        int c;
//        while ((c = bufferedReader.read()) != -1)
//            output.append((char) c);
//        return new ObjectMapper().readValue(output.toString(), StandardResponse.class);
//
//    }

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
            if (entityManager.isOpen())
                entityManager.close();
        }

    }
}