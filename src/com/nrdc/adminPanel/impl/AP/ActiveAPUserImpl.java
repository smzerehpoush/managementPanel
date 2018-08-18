package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestActiveUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseActiveUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class ActiveAPUserImpl {
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseActiveUser> activeUser(RequestActiveUser request) throws Exception {

        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);

            Query query = entityManager.createQuery("UPDATE APUser u SET u.isActive = true WHERE u.id = :id")
                    .setParameter("id", request.getFkUserId());
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
                entityManager.getEntityManagerFactory().getCache().evictAll();
                query.executeUpdate();
                transaction.commit();
            }

            VTResponse<ResponseActiveUser> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
            throw ex;
        }
    }
}