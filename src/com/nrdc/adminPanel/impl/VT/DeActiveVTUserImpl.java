package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestDeActiveUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseDeActiveUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class DeActiveVTUserImpl {

    public VTResponse<ResponseDeActiveUser> deActiveUser(RequestDeActiveUser request) throws Exception {
        EntityManager entityManager = DBManager.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            entityManager.createQuery("UPDATE VTUser u SET u.isActive = false WHERE u.id = :id ")
                    .setParameter("id", request.getFkUserId())
                    .executeUpdate();
            entityManager.createQuery("DELETE FROM VTToken WHERE fkUserId = :fkUserId")
                    .setParameter("fkUserId", request.getFkUserId())
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();
            VTResponse<ResponseDeActiveUser> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new VTResponse<ResponseDeActiveUser>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}