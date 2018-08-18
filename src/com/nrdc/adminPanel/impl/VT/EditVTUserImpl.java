package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestEditVTUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseEditUser;
import com.nrdc.adminPanel.model.VTUser;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class EditVTUserImpl {
    private Logger logger = Logger.getLogger(EditVTUserImpl.class.getName());

    public VTResponse<ResponseEditUser> editUser(RequestEditVTUser request) throws Exception {
        EntityManager entityManager = DBManager.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery("SELECT u FROM VTUser u WHERE u.policeCode = " + request.getPoliceCode());
            VTUser oldVTUser = (VTUser) query.getSingleResult();

            System.out.println("firstName : " + request.getFirstName());
            entityManager.createQuery("UPDATE VTUser u SET u.firstName = '" + request.getFirstName() + "' WHERE u.policeCode=" + request.getPoliceCode()).executeUpdate();

            System.out.println("lastName : " + request.getLastName());
            entityManager.createQuery("UPDATE VTUser u SET u.lastName = '" + request.getLastName() + "' WHERE u.policeCode=" + request.getPoliceCode()).executeUpdate();

            System.out.println("phone Number : " + request.getPhoneNumber());
            entityManager.createQuery("UPDATE VTUser u SET u.phoneNumber = '" + request.getPhoneNumber() + "' WHERE u.policeCode=" + request.getPoliceCode()).executeUpdate();

            System.out.println("nationalId :" + request.getNationalId());
            int size = entityManager.createQuery("SELECT u FROM VTUser u WHERE u.nationalId = '" + request.getNationalId() + "'").getResultList().size();
            if (size != 1 && size != 0) {
                logger.error(Constants.NOT_UNIQUE_NATIONAL_ID);
                throw new Exception(Constants.NOT_UNIQUE_NATIONAL_ID);
            }
            if (oldVTUser.getNationalId() != null && !oldVTUser.getNationalId().equals(request.getNationalId()))
                entityManager.createQuery("UPDATE VTUser u SET u.nationalId = '" + request.getNationalId() + "' WHERE u.policeCode=" + request.getPoliceCode()).executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();
            else {
                transaction = entityManager.getTransaction();
                transaction.commit();
            }
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