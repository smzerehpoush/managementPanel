package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogout;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import com.nrdc.policeHamrah.model.dao.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class LogoutImpl {

    public StandardResponse logout(RequestLogout request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();

            SystemDao systemDao = SystemDao.getSystem(request.getFkSystemId());
            SystemDao phSystemDao = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
            UserDao user = UserDao.getUser(request.getToken(), phSystemDao.getId());
            if (systemDao.getSystemName().equals(SystemNames.POLICE_HAMRAH.name())) {
                logoutFromPH(user, entityManager);
            } else {
                deleteToken(user, systemDao, entityManager);
                deleteKey(user, systemDao, entityManager);
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

    private void logoutFromPH(UserDao user, EntityManager entityManager) {

        entityManager.createQuery("DELETE FROM TokenDao WHERE fkUserId = :fkUserId ")
                .setParameter("fkUserId", user.getId())
                .executeUpdate();
        entityManager.createQuery("DELETE FROM KeyDao WHERE fkUserId = :fkUserId ")
                .setParameter("fkUserId", user.getId())
                .executeUpdate();
    }

    private void deleteToken(UserDao user, SystemDao systemDao, EntityManager entityManager) {
        entityManager.createQuery("DELETE FROM TokenDao WHERE fkUserId = :fkUserId AND fkSystemId = :fkSystemId")
                .setParameter("fkUserId", user.getId())
                .setParameter("fkSystemId", systemDao.getId())
                .executeUpdate();

    }

    private void deleteKey(UserDao user, SystemDao systemDao, EntityManager entityManager) throws Exception {
        entityManager.createQuery("DELETE FROM KeyDao WHERE fkUserId = :fkUserId AND fkSystemId = :fkSystemId")
                .setParameter("fkSystemId", systemDao.getId())
                .setParameter("fkUserId", user.getId())
                .executeUpdate();

    }
}
