package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.TokenRequest;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAdminLogout;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class APLogoutImpl {
    private static Logger logger = Logger.getLogger(APLogoutImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseAdminLogout> logout(TokenRequest request) throws Exception {
        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);
            logger.info("token : " + token);
            long fkUserId = Database.getFkUserIdByAdminToken(token);
            logger.info("fk user id : " + fkUserId);

            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery("DELETE FROM APToken WHERE fkUserId =:fkUserId")
                    .setParameter("fkUserId", fkUserId);
            query.executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();
            VTResponse<ResponseAdminLogout> vtResponse = new VTResponse<>();
            vtResponse.setResultCode(1);
            vtResponse.setResultMessage("OK");
            return vtResponse;

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
