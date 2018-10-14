package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestLogout;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestLogout;
import com.nrdc.managementPanel.model.dao.User;
import com.nrdc.managementPanel.model.dao.System;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class LogoutImpl {

    public StandardResponse logout(RequestLogout request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();

            System system = System.getSystem(request.getFkSystemId());
            System phSystem = System.getSystem(SystemNames.POLICE_HAMRAH);
            User user = User.getUser(request.getToken(), phSystem.getId());
            if (system.getSystemName().equals(SystemNames.POLICE_HAMRAH.name())) {
                logoutFromPH(user, entityManager);
            } else {
                deleteToken(user, system, entityManager);
                deleteKey(user, system, entityManager);
            }
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return new StandardResponse();

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void logoutFromPH(User user, EntityManager entityManager) {

        entityManager.createQuery("DELETE FROM Token WHERE fkUserId = :fkUserId ")
                .setParameter("fkUserId", user.getId())
                .executeUpdate();
        entityManager.createQuery("DELETE FROM Key WHERE fkUserId = :fkUserId ")
                .setParameter("fkUserId", user.getId())
                .executeUpdate();
    }

    private void deleteToken(User user, System system, EntityManager entityManager) {
        entityManager.createQuery("DELETE FROM Token WHERE fkUserId = :fkUserId AND fkSystemId = :fkSystemId")
                .setParameter("fkUserId", user.getId())
                .setParameter("fkSystemId", system.getId())
                .executeUpdate();

    }

    private void deleteKey(User user, System system, EntityManager entityManager) throws Exception {
        entityManager.createQuery("DELETE FROM Key WHERE fkUserId = :fkUserId AND fkSystemId = :fkSystemId")
                .setParameter("fkSystemId", system.getId())
                .setParameter("fkUserId", user.getId())
                .executeUpdate();

    }
}
