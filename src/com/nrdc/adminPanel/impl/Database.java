package com.nrdc.adminPanel.impl;


import com.nrdc.adminPanel.exceptions.NotValidTokenException;
import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestBasicInfo;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestEnquireNumbering;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class Database {

    static Logger logger = Logger.getLogger(Database.class.getName());

    /***
     * checks that token is valid or not
     * @param token user token
     * @throws NotValidTokenException throws exception if token is not valid
     */
    public static void tokenValidation(String token) throws NotValidTokenException {
        EntityManager entityManager = DBManager.getEntityManager();
        try {
            logger.info("=======[ TOKEN VALIDATION ]=======");
            logger.info("VTToken : " + token);
            Query tokenValidationQuery = entityManager.createQuery("SELECT it FROM VTToken it WHERE it.token= :token")
                    .setParameter("token", token);
            List result = tokenValidationQuery.getResultList();
            if (result.size() == 0) {
                logger.info("token is not valid");
                throw new NotValidTokenException(Constants.NOT_VALID_TOKEN);
            } else {
                logger.info("token is valid");
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static void adminTokenValidation(String token) throws NotValidTokenException {
        EntityManager entityManager = DBManager.getEntityManager();

        try {
            logger.info("=======[ ADMIN TOKEN VALIDATION : START ]=======");
            logger.info("VTToken : " + token);
            Query tokenValidationQuery = entityManager.createQuery("SELECT it FROM APToken it WHERE it.token='" + token + "'");
            List result = tokenValidationQuery.getResultList();
            if (result.size() == 0) {
                logger.info("token is not valid");
                throw new NotValidTokenException(Constants.NOT_VALID_TOKEN);
            } else {
                logger.info("token is valid");
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }


    /***
     *
     * @param token user token
     * @return returns user id
     * @throws Exception throws exception if token is not valid or user id not exists
     */
    public static Long getFkUserIdByToken(String token) throws Exception {
        EntityManager entityManager = DBManager.getEntityManager();

        try {
            logger.info("=======[ GET FK_USER_ID BY TOKEN ]=======");
            tokenValidation(token);
            Query query = entityManager.createQuery("SELECT it.fkUserId from VTToken it where it.token= :token")
                    .setParameter("token", token);
            Long userId = (Long) query.getSingleResult();
            logger.info("VTToken : " + token);
            logger.info("VTUser Id : " + userId);
            return userId;

        } catch (Exception ex) {
            logger.info(Constants.NO_USER_FOR_TOKENT);
            throw new Exception(Constants.NO_USER_FOR_TOKENT);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static Long getFkUserIdByAdminToken(String token) throws Exception {
        EntityManager entityManager = DBManager.getEntityManager();

        try {
            logger.info("=======[ GET FK_USER_ID BY TOKEN ]=======");
            adminTokenValidation(token);
            Query query = entityManager.createQuery("SELECT it.fkUserId from APToken it where it.token='" + token + "'");
            Long userId = (Long) query.getSingleResult();
            logger.info("Admin VTToken : " + token);
            logger.info("VTUser Id : " + userId);
            return userId;

        } catch (Exception ex) {
            logger.info(Constants.NO_USER_FOR_TOKENT);
            throw new Exception(Constants.NO_USER_FOR_TOKENT);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    /***
     *
     * @param token user token
     * @return returns user police-code with token
     * @throws Exception throws exception if token is not valid or police code not exists
     */
    public static Long getFkPoliceCodeByToken(String token) throws Exception {
        EntityManager entityManager = DBManager.getEntityManager();
        try {
            logger.info("=======[ GET POLICE CODE BY TOKEN ]=======");
            logger.info("VTToken : " + token);
            tokenValidation(token);
            Long userId = getFkUserIdByToken(token);
            Query getPoliceCodeQuery = entityManager.createQuery("SELECT user.policeCode from VTUser user where user.id=" + userId);
            Long policeCode = Long.parseLong((String) getPoliceCodeQuery.getSingleResult());
            logger.info("token : " + token);
            logger.info("Police Code : " + policeCode);
            return policeCode;

        } catch (Exception ex) {
            logger.info(Constants.USER_IS_NOT_VALID);
            throw new Exception(Constants.USER_IS_NOT_VALID);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public VTResponse getAllBasicInfo(RequestBasicInfo requestBasicInfo) {

        try {
            VTResponse vtResponse = new VTResponse();
            vtResponse.setResultCode(1);
            vtResponse.setResultMessage("OK");
            return vtResponse;
        } catch (Exception exception) {
            logger.error(exception);
            throw exception;
        }

    }

    public VTResponse enquireNumbering(RequestEnquireNumbering requestEnquireNumbering) {
        try {
            VTResponse vtResponse = new VTResponse();
            vtResponse.setResultCode(1);
            vtResponse.setResultMessage("OK");
            return vtResponse;
        } catch (Exception exeption) {
            throw exeption;
        }

    }


}