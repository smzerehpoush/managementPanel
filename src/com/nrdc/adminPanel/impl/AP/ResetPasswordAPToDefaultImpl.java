package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestResetPasswordDefault;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseResetPasswordDefault;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ResetPasswordAPToDefaultImpl {
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseResetPasswordDefault> resetPassword(RequestResetPasswordDefault requestResetPasswordDefault) {
        try {
            String token = requestResetPasswordDefault.getToken();
            Long fkUserId = requestResetPasswordDefault.getFkUserId();
            Database.adminTokenValidation(token);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            entityManager.createQuery("UPDATE APUser u SET u.password = '123' WHERE u.id = :id")
                    .setParameter("id", fkUserId)
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();
            VTResponse response = new VTResponse();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;
        } catch (Exception ex) {
            return new VTResponse<ResponseResetPasswordDefault>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
