package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestRemoveToken;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import com.nrdc.policeHamrah.model.dao.TokenDao;
import com.nrdc.policeHamrah.model.dao.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TokenImpl {
    public StandardResponse removeToken(String token, RequestRemoveToken requestRemoveToken) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            UserDao user = UserDao.validate(token);
            SystemDao us = SystemDao.getSystem(requestRemoveToken.getFkSystemId());
            TokenDao.validateToken(requestRemoveToken.getToken(), us.getSystemName());

            Long fkUserId = (Long) entityManager.createQuery("SELECT t.fkUserId FROM TokenDao t WHERE t.token = :token AND t.fkSystemId = :fkSystemId")
                    .setParameter("token", requestRemoveToken.getToken())
                    .setParameter("fkSystemId", requestRemoveToken.getFkSystemId())
                    .getSingleResult();
            if (user.getId().equals(fkUserId)) {
                throw new Exception(Constants.CAN_NOT_REMOVE_YOUR_TOKEN);
            }
            SystemDao systemDao = SystemDao.getSystem(requestRemoveToken.getFkSystemId());
            String privilegeName = "REMOVE_" + systemDao.getSystemName() + "_TOKENS";
            user.checkPrivilege(privilegeName);
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("DELETE FROM TokenDao WHERE token = :token AND fkSystemId = :fkSystemId")
                    .setParameter("token", requestRemoveToken.getToken())
                    .setParameter("fkSystemId", requestRemoveToken.getFkSystemId())
                    .executeUpdate();
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();


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

}
