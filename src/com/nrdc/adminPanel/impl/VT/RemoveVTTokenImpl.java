package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestRemoveToken;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseRemoveToken;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class RemoveVTTokenImpl {

    public VTResponse<ResponseRemoveToken> removeVTToken(RequestRemoveToken request) throws Exception {

        EntityManager entityManager = DBManager.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            String token = request.getToken();
            Long fkUserId = request.getFkUserId();
            Database.adminTokenValidation(token);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            entityManager.createQuery("DELETE FROM VTToken WHERE fkUserId = :fkUserId")
                    .setParameter("fkUserId", fkUserId)
                    .executeUpdate();

            if (transaction != null && transaction.isActive())
                transaction.commit();

            VTResponse<ResponseRemoveToken> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new VTResponse<ResponseRemoveToken>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}