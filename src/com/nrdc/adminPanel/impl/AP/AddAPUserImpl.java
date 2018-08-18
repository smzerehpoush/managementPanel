package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestAddAPUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAddUser;
import com.nrdc.adminPanel.model.APUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class AddAPUserImpl {
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseAddUser> addUser(RequestAddAPUser request) throws Exception {
        String token = request.getToken();
        try {
            Database.adminTokenValidation(token);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            APUser user = request.getUser();
            checkUsername(user.getUsername());
            checkNationalId(user.getNationalId());
            entityManager.persist(user);
            if (transaction != null && transaction.isActive())
                transaction.commit();

            VTResponse<ResponseAddUser> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new VTResponse<ResponseAddUser>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkUsername(String username) throws Exception {
        Long size = (Long) entityManager.createQuery("SELECT count (u) FROM  APUser u WHERE u.isActive = true  AND u.username = :username")
                .setParameter("username", username)
                .getSingleResult();
        if (size > 0)
            throw new Exception(Constants.NOT_ACTIVE_USER);
    }

    private void checkNationalId(String nationalId) throws Exception {
        if (nationalId.length() != 10) {
            throw new Exception(Constants.NOT_VALID_NATIONAL_ID);
        }
        Long size = (Long) entityManager.createQuery("SELECT count (u) FROM  APUser u WHERE u.isActive = true  AND u.nationalId = :nationalId")
                .setParameter("nationalId", nationalId)
                .getSingleResult();
        if (size > 0)
            throw new Exception(Constants.NOT_ACTIVE_USER);
    }
}