package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestRemoveToken;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseRemoveToken;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class RemoveAPTokenImpl {
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseRemoveToken> removeAPToken(RequestRemoveToken request) throws Exception {

        try {
            String token = request.getToken();
            Long fkUserId = request.getFkUserId();
            Database.adminTokenValidation(token);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery("SELECT t.token FROM APToken t WHERE t.fkUserId = " + fkUserId);
            try {
                String dbToken = (String) query.getSingleResult();
                if (token.equals(dbToken))
                    throw new Exception(Constants.CAN_NOT_DELETE_TOKEN);

            } catch (NoResultException ex) {

            }
            query = entityManager.createQuery("DELETE FROM APToken WHERE fkUserId = :fkUserId")
                    .setParameter("fkUserId", fkUserId);
            query.executeUpdate();
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