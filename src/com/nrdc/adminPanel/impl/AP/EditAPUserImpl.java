package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestEditAPUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseEditUser;
import com.nrdc.adminPanel.model.APUser;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class EditAPUserImpl {
    private static Logger logger = Logger.getLogger(EditAPUserImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseEditUser> editUser(RequestEditAPUser request) throws Exception {

        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery("SELECT u FROM APUser u WHERE u.username = :username")
                    .setParameter("username", request.getUsername());
            APUser oldAPUser = (APUser) query.getSingleResult();

            System.out.println("firstName : " + request.getFirstName());
            entityManager.createQuery("UPDATE APUser u SET u.firstName = :firstName WHERE u.username= :username")
                    .setParameter("firstName", request.getFirstName())
                    .setParameter("username", request.getUsername())
                    .executeUpdate();

            System.out.println("lastName : " + request.getLastName());
            entityManager.createQuery("UPDATE APUser u SET u.lastName = :lastName WHERE u.username= :username")
                    .setParameter("lastName", request.getLastName())
                    .setParameter("username", request.getUsername())
                    .executeUpdate();

            System.out.println("phone Number : " + request.getPhoneNumber());
            entityManager.createQuery("UPDATE APUser u SET u.phoneNumber = :phoneNumber WHERE u.username= :username")
                    .setParameter("phoneNumber", request.getPhoneNumber())
                    .setParameter("username", request.getUsername())
                    .executeUpdate();

            System.out.println("nationalId :" + request.getNationalId());
            int size = entityManager.createQuery("SELECT u FROM APUser u WHERE u.nationalId = :nationalId")
                    .setParameter("nationalId", request.getNationalId())
                    .getResultList().size();
            if (size != 1 && size != 0) {
                logger.error(Constants.NOT_UNIQUE_NATIONAL_ID);
                throw new Exception(Constants.NOT_UNIQUE_NATIONAL_ID);
            }
            if (oldAPUser.getNationalId() != null && !oldAPUser.getNationalId().equals(request.getNationalId()))
                entityManager.createQuery("UPDATE APUser u SET u.nationalId = :nationalId WHERE u.username = :username")
                        .setParameter("nationalId", request.getNationalId())
                        .setParameter("username", request.getUsername())
                        .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();
            VTResponse<ResponseEditUser> response = new VTResponse<>();
            response.setResultMessage("OK");
            response.setResultCode(1);
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new VTResponse<ResponseEditUser>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}