package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestRemoveRole;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseRemoveRole;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class RemoveRoleImpl {
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseRemoveRole> removeRole(RequestRemoveRole requestRemoveRole) throws Exception {

        try {
            String token = requestRemoveRole.getToken();
            Database.adminTokenValidation(token);
            Long roleId = requestRemoveRole.getRoleId();
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            entityManager.createQuery("DELETE FROM VTRole WHERE id = :roleId")
                    .setParameter("roleId", roleId)
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();

            VTResponse<ResponseRemoveRole> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}