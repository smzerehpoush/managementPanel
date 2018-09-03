package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestRemoveToken;
import com.nrdc.managementPanel.model.dto.System;
import com.nrdc.managementPanel.model.dto.Token;
import com.nrdc.managementPanel.model.dto.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TokenImpl {
    public StandardResponse removeToken(String token, RequestRemoveToken requestRemoveToken) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            User user = User.getUser(token, SystemNames.MANAGEMENT_PANEL);
            System us = System.getSystem(requestRemoveToken.getFkSystemId());
            Token.validateToken(requestRemoveToken.getToken(), us.getSystemName());

            Long fkUserId = (Long) entityManager.createQuery("SELECT t.fkUserId FROM Token t WHERE t.token = :token AND t.fkSystemId = :fkSystemId")
                    .setParameter("token", requestRemoveToken.getToken())
                    .setParameter("fkSystemId", requestRemoveToken.getFkSystemId())
                    .getSingleResult();
            if (user.getId().equals(fkUserId)) {
                throw new Exception(Constants.CAN_NOT_REMOVE_YOUR_TOKEN);
            }
            System system = System.getSystem(requestRemoveToken.getFkSystemId());
            String privilegeName = "REMOVE_" + system.getSystemName() + "_TOKENS";
            user.checkPrivilege(privilegeName);
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("DELETE FROM Token WHERE token = :token AND fkSystemId = :fkSystemId")
                    .setParameter("token", requestRemoveToken.getToken())
                    .setParameter("fkSystemId", requestRemoveToken.getFkSystemId())
                    .executeUpdate();
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

}
