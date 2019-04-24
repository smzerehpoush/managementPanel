package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.*;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAuthenticateUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.policeHamrah.model.dao.*;
import com.nrdc.policeHamrah.model.dto.AuthDto;
import com.nrdc.policeHamrah.model.dto.UserDto;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Date;

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
                response.setResultMessage(Constants.USER + Constants.IS_NOT_VALID);
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
                throw new ServerException(Constants.YOU_HAVE_PH_USER);

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
        StandardResponse response = MyGsonBuilder.build().fromJson(output, StandardResponse.class);
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
        throw new ServerException(Constants.SYSTEM_IMPLEMENTATION_NOT_ADDED);
    }

    /**
     * @param requestLogin username, phoneNumber, password to login into Police Hamrah
     * @return StandardResponse with value of {@link ResponseLogin}
     */
    public StandardResponse login(RequestLogin requestLogin) {
        EntityManager entityManager = Database.getEntityManager();
        EntityManager entityManagerLog = Database.getEntityManager();
        EntityManager entityManagerUserActivities = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        EntityTransaction transaction = entityManager.getTransaction();
        EntityTransaction logTransaction = entityManagerLog.getTransaction();
        EntityTransaction activitiesTransaction = entityManagerUserActivities.getTransaction();
        LogLoginDao log = new LogLoginDao();
        log.setDescription("LOGIN");
        log.setId(IDBuilder.generateNewID());
        log.setTime(new Date());
        log.setUsername(requestLogin.getUsername());
        try {
            if (!transaction.isActive())
                transaction.begin();
            if (!logTransaction.isActive())
                logTransaction.begin();
            if (!activitiesTransaction.isActive())
                activitiesTransaction.begin();
            SystemDao system = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
            Long size = (Long) entityManager.createQuery("SELECT COUNT (a) FROM AuthDao a " +
                    "JOIN UserDao u ON u.id = a.fkUserId " +
                    "WHERE a.fkSystemId = :fkSystemId " +
                    "AND (u.phoneNumber = :phoneNumber OR u.username = :username )")
                    .setParameter("phoneNumber", requestLogin.getPhoneNumber())
                    .setParameter("fkSystemId", system.getId())
                    .setParameter("phoneNumber", requestLogin.getPhoneNumber())
                    .setParameter("username", requestLogin.getUsername())
                    .getSingleResult();
            if (!size.equals(0L))
                throw new ServerException(Constants.USER + Constants.IS_ACTIVE);
            UserDao user = verifyUser(requestLogin);
            SystemDao systemDao = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
            log.setFirstName(user.getFirstName());
            log.setLastName(user.getLastName());
            log.setFkUserId(user.getId());
            log.setNationalId(user.getNationalId());
            log.setPhoneNumber(user.getPhoneNumber());
            log.setPoliceCode(user.getPoliceCode());
            log.setFkProvinceId(user.getFkProvinceId());
            log.setStatusCode(1L);
            AuthDto auth = new AuthDao(user, systemDao);
            entityManager.persist(auth);
            ResponseLogin responseLogin = new ResponseLogin();
            responseLogin.setKey(auth.getKey());
            responseLogin.setToken(auth.getToken());
            responseLogin.setUser(user.createCustomUser());
            StandardResponse<ResponseLogin> response = new StandardResponse<>();
            response.setResponse(responseLogin);
            LogUserActivitiesDao logUserActivities = new LogUserActivitiesDao();
            logUserActivities.setId(IDBuilder.generateNewID());
            logUserActivities.setActionType("LOGIN");
            logUserActivities.setFkUserId(user.getId());
            logUserActivities.setActionId(log.getId());
            entityManagerUserActivities.persist(logUserActivities);
//            log.setId();
//            logUserActivities.setActionId();
            entityManagerLog.persist(log);
            if (transaction.isActive())
                transaction.commit();
            if (logTransaction.isActive())
                logTransaction.commit();
            if (activitiesTransaction.isActive())
                activitiesTransaction.commit();
            return response;
        } catch (Exception ex) {
            log.setStatusCode(-1L);
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
            if (entityManagerLog.isOpen())
                entityManagerLog.close();
            if (entityManagerUserActivities.isOpen())
                entityManagerUserActivities.close();
        }
    }

    /***
     * this service calls by other servers like nazer, agahi,...
     * @param policeHamrahToken token of user in policeHamrah system
     * @param fkSystemId id of system that user wants to login to it
     * @return ResponseLogin
     */
    public StandardResponse loginToSystem(String policeHamrahToken, Long fkSystemId) {
        EntityManager entityManager = Database.getEntityManager();
        EntityManager entityManagerLog = Database.getEntityManager();
        EntityManager entityManagerUserActivities = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        EntityTransaction transactionLog = entityManagerLog.getTransaction();
        EntityTransaction transactionUserActivities = entityManagerLog.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        LogLoginDao logLogin = new LogLoginDao();
        logLogin.setId(IDBuilder.generateNewID());
        logLogin.setTime(new Date());
        logLogin.setToken(policeHamrahToken);
        logLogin.setFkSystemId(fkSystemId);
        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
            }
            if (transactionLog != null && !transactionLog.isActive()) {
                transactionLog.begin();
            }
            if (transactionUserActivities != null && !transactionUserActivities.isActive()) {
                transactionUserActivities.begin();
            }
            UserDao user = UserDao.validate(policeHamrahToken);
            if (!user.getIsActive())
                throw new ServerException(Constants.USER + Constants.IS_NOT_ACTIVE);
            logLogin.setFkProvinceId(user.getFkProvinceId());
            logLogin.setPoliceCode(user.getPoliceCode());
            logLogin.setPhoneNumber(user.getPhoneNumber());
            logLogin.setNationalId(user.getNationalId());
            logLogin.setFkUserId(user.getId());
            logLogin.setLastName(user.getLastName());
            logLogin.setFirstName(user.getFirstName());
            logLogin.setUsername(user.getUsername());
            logLogin.setDescription("LOGIN_TO_SYSTEM");
            logLogin.setTime(new Date());
            LogUserActivitiesDao logUserActivitiesDao = new LogUserActivitiesDao();
            logUserActivitiesDao.setId(IDBuilder.generateNewID());
            logUserActivitiesDao.setActionType("LOGIN_TO_SYSTEM");
            logUserActivitiesDao.setActionId(logLogin.getId());
            logUserActivitiesDao.setFkUserId(user.getId());
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            logLogin.setSystemName(systemDao.getSystemName());
            entityManager.createQuery("DELETE FROM AuthDao WHERE fkUserId = :fkUserId AND fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", user.getId())
                    .setParameter("fkSystemId", fkSystemId)
                    .executeUpdate();
            AuthDao auth = new AuthDao(user, systemDao);
            entityManager.persist(auth);
            StandardResponse<ResponseLogin> response = loginToSystem(user, systemDao, auth);
            logLogin.setStatusCode(1L);
            entityManagerLog.persist(logLogin);
            entityManagerUserActivities.persist(logUserActivitiesDao);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            if (transactionLog != null && transactionLog.isActive())
                transactionLog.commit();
            return response;
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            logLogin.setStatusCode(-1L);
            entityManagerLog.persist(logLogin);
            if (transactionLog != null && transactionLog.isActive())
                transactionLog.commit();
            return StandardResponse.getNOKExceptions(exception);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
            if (entityManagerLog.isOpen())
                entityManagerLog.close();
        }
    }

    private StandardResponse<ResponseLogin> loginToSystem(UserDao user, SystemDao systemDao, AuthDao auth) throws Exception {

        return createResponseLogin(user, systemDao, auth);
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
//            throw new ServerException(Constants.SYSTEM + Constants.IS_NOT_VALID);
//        }
        responseLogin.setUser(user);
        response.setResponse(responseLogin);
        return response;
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
            throw new ServerException(Constants.USER + Constants.IS_NOT_ACTIVE);
    }

    private String decryptPassword(String username, String encryptedPassword) throws Exception {
        String key = getUserPassword(username);
        return Encryption.decryptPassword(key, encryptedPassword);
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
//            throw new ServerException("Can not add token to system : " + connection.getResponseCode());
//        }
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuilder output = new StringBuilder();
//        int c;
//        while ((c = bufferedReader.read()) != -1)
//            output.append((char) c);
//        return new ObjectMapper().readValue(output.toString(), StandardResponse.class);
//
//    }

    private String getUserPassword(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (String) entityManager.createQuery("SELECT u.password FROM UserDao u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (Exception ex) {
            throw new ServerException(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

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
}