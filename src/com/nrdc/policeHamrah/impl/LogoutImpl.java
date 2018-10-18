package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogout;
import com.nrdc.policeHamrah.model.dao.OperationDao;
import com.nrdc.policeHamrah.model.dao.PrivilegeDao;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import com.nrdc.policeHamrah.model.dao.UserDao;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class LogoutImpl {

    public StandardResponse logout(String token, RequestLogout request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityManager operationEntityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        EntityTransaction operationTransaction = operationEntityManager.getTransaction();
        OperationDao operation = new OperationDao();
        operation.setUserToken(token);
        operation.setPrivilegeName(PrivilegeNames.LOGOUT.name());
        String description = new StringBuilder().append("someone wants to logout with token : ")
                .append(token)
                .append(" ,systemId : ")
                .append(request.getFkSystemId())
                .toString();
        operation.setDescription(description);
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            if (!operationTransaction.isActive())
                operationTransaction.begin();
            PrivilegeDto privilege = PrivilegeDao.getPrivilege(PrivilegeNames.LOGOUT);
            operation.setFkPrivilegeId(privilege.getId());
            UserDao user = UserDao.getUser(token);
            if (request.getFkSystemId() != null) {
                SystemDao systemDao = SystemDao.getSystem(request.getFkSystemId());
                if (systemDao.getSystemName().equals(SystemNames.POLICE_HAMRAH.name())) {
                    logoutFromPH(user, entityManager);
                } else {
                    deleteToken(user, systemDao, entityManager);
                    deleteKey(user, systemDao, entityManager);
                }
            } else {
                logoutFromPH(user, entityManager);

            }
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return new StandardResponse();

        } catch (Exception ex) {
            operation.setStatusCode(-1L);
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            operationEntityManager.persist(operation);
            if (operationTransaction.isActive())
                operationTransaction.commit();
            if (entityManager.isOpen())
                entityManager.close();
            if (operationEntityManager.isOpen())
                operationEntityManager.close();
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
