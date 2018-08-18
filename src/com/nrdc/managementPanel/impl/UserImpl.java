package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestActiveUser;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseActiveUser;
import com.nrdc.managementPanel.model.Token;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class UserImpl {
    public StandardResponse<ResponseActiveUser> activeUser(String token, RequestActiveUser request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Token.validateToken(token, SystemNames.MANAGEMENT_PANEL);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.isActive = true WHERE u.id = (SELECT t.fkUserId FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM Systems s WHERE s.systemName = :systemName))")
                    .setParameter("token", token)
                    .setParameter("systemName", SystemNames.MANAGEMENT_PANEL);
            if (transaction != null && transaction.isActive())
                transaction.commit();

            StandardResponse<ResponseActiveUser> response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
            return StandardResponse.getNOKExceptions(ex);
        }
    }
}
