package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestResetPasswordDefault;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseResetPasswordDefault;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ResetPasswordVTToDefaultImpl {

    public VTResponse<ResponseResetPasswordDefault> resetPassword(RequestResetPasswordDefault requestResetPasswordDefault) {
        EntityManager entityManager = DBManager.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            String token = requestResetPasswordDefault.getToken();
            Long fkUserId = requestResetPasswordDefault.getFkUserId();
            Database.adminTokenValidation(token);
            entityManager.getEntityManagerFactory().getCache().evictAll();
            entityManager.createQuery("UPDATE VTUser u SET u.password = '123' WHERE u.id = :id")
                    .setParameter("id", fkUserId)
                    .executeUpdate();
            VTResponse response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new VTResponse<ResponseResetPasswordDefault>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
