package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestAdminLogin;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAdminLogin;
import com.nrdc.adminPanel.model.APToken;
import com.nrdc.adminPanel.model.APUser;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

public class APLoginImpl {
    private static Logger logger = Logger.getLogger(APLoginImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();


    public VTResponse<ResponseAdminLogin> adminLogin(RequestAdminLogin requestAdminLogin) throws Exception {
        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
                entityManager.getEntityManagerFactory().getCache().evictAll();
            }
            String username = requestAdminLogin.getUsername();
            logger.info("username : " + username);
            String password = requestAdminLogin.getPassword();
            logger.info("password : " + password);
            APUser apUser = userAuthentication(username, password);
            if (!apUser.getIsActive()) {
                logger.info(Constants.USER_IS_NOT_EXISTS);
                throw new Exception(Constants.USER_IS_NOT_EXISTS);
            }
            logger.info("APUser authentication successful");
            checkUserToken(apUser);
            APToken token = new APToken();
            token.setFkUserId(apUser.getId());
            token.setStartDate(new Date());
            token.setToken(generateToken());
            entityManager.persist(token);
            setUserLastLogin(requestAdminLogin);
            ResponseAdminLogin responseAdminLogin = new ResponseAdminLogin();
            responseAdminLogin.setUser(apUser);
            responseAdminLogin.setToken(token.getToken());
            VTResponse<ResponseAdminLogin> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseAdminLogin);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return response;
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new VTResponse<ResponseAdminLogin>().getNOKExceptions(exception.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    private APUser userAuthentication(String username, String password) throws Exception {
        try {
            Query query = entityManager.createQuery("SELECT u FROM APUser u WHERE u.username=:username and u.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password);
            APUser apUser = (APUser) query.getSingleResult();
            if (!apUser.getIsActive()) {
                throw new Exception(Constants.USER_IS_NOT_ACTIVE);
            }
            return apUser;
        } catch (NoResultException ex1) {
            throw new Exception(Constants.USER_PASSWORD_INCORRECT);
        } catch (NonUniqueResultException ex2) {
            throw new Exception(Constants.USER_IS_NOT_VALID);
        }

    }

    private void checkUserToken(APUser user) throws Exception {
        Query query = entityManager.createQuery("SELECT  t FROM APToken t WHERE t.fkUserId = :fkUserId")
                .setParameter("fkUserId", user.getId());
        List result = query.getResultList();
        if (result.size() != 0)
            throw new Exception(Constants.ACTIVE_USER_EXISTS);
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(40, random).toString(32);
    }

    private void setUserLastLogin(RequestAdminLogin requestAdminLogin) throws Exception {
        try {
            Query query = entityManager.createQuery("UPDATE APUser u SET  u.lastLogin = :lastLogin WHERE u.username='" + requestAdminLogin.getUsername() + "'" + " and u.password = '" + requestAdminLogin.getPassword() + "'");
            query.setParameter("lastLogin", new Date(System.currentTimeMillis()), TemporalType.TIMESTAMP);
            query.executeUpdate();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
