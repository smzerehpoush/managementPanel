package com.nrdc.policeHamrah.impl;

import com.google.gson.Gson;
import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.*;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.model.dao.*;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class LogoutImpl {

    public StandardResponse logout(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityManager operationEntityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        EntityTransaction operationTransaction = operationEntityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        OperationDao operation = new OperationDao();
        operation.setUserToken(token);
        operation.setPrivilegeName(PrivilegeNames.LOGOUT.name());
        String description = new StringBuilder().append("someone wants to logout with token : ")
                .append(token)
                .append(" ,systemId : ")
                .append(fkSystemId)
                .toString();
        operation.setDescription(description);
        try {
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            if (!operationTransaction.isActive())
                operationTransaction.begin();
            PrivilegeDto privilege = PrivilegeDao.getPrivilege(PrivilegeNames.LOGOUT);
            operation.setFkPrivilegeId(privilege.getId());
            UserDao user = UserDao.validate(token);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            SystemDao phSystem = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
            String anotherSystemToken;
            try {
                anotherSystemToken = getUserTokenInSystem(user, systemDao);
            } catch (Exception ex) {
                return new StandardResponse();
            }
            List<AuthDao> authDaoList = entityManager.createQuery("SELECT (a) FROM AuthDao a WHERE a.fkUserId = :fkUserId ")
                    .setParameter("fkUserId", user.getId())
                    .getResultList();
            int count = 0;
            for (int i = 0; i < authDaoList.size(); i++) {
                AuthDao authDao = authDaoList.get(i);
                if (!authDao.getFkSystemId().equals(phSystem.getId()))
                    count++;
            }

            if (systemDao.getSystemName().equals(SystemNames.POLICE_HAMRAH.name())) {
                if (count > 0)
                    throw new ServerException(Constants.USER_IS_IN_ANOTHER_SYSTEM);
            } else {
                deleteAuthInfoFromAnotherSystemDatabase(anotherSystemToken, systemDao);
            }
            deleteAuthInfoInPHDatabase(user, systemDao, entityManager);
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return new StandardResponse<>();

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

    private String getUserTokenInSystem(UserDao user, SystemDao systemDao) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (String) entityManager.createQuery("SELECT a.token FROM AuthDao a WHERE a.fkUserId = :fkUserId AND a.fkSystemId = :fkSystemId ")
                    .setParameter("fkSystemId", systemDao.getId())
                    .setParameter("fkUserId", user.getId())
                    .getSingleResult();
        } catch (Exception ex) {
            throw new ServerException(Constants.TOKEN + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    void deleteAuthInfoFromAnotherSystemDatabase(String token, SystemDao systemDao) throws Exception {
        TokenRequest request = new TokenRequest();
        request.setToken(token);
        EncryptedResponse encryptedRequest = Encryption.encryptResponse(Constants.DEFAULT_KEY, request);
        String output = CallWebService.callPostService(systemDao.getSystemPath() + Constants.LOGOUT_PATH, encryptedRequest);
        EncryptedRequest encryptedResponse = new Gson().fromJson(output, EncryptedRequest.class);
        encryptedResponse.setToken(Constants.DEFAULT_KEY);
        StandardResponse response = new Gson().fromJson(Encryption.decryptRequest(encryptedResponse), StandardResponse.class);
        if (response.getResultCode() == -1)
            throw new ServerException(Constants.CAN_NOT_LOGOUT_FROM_SYSTEM + systemDao.getTitle());

    }

    private void deleteAuthInfoInPHDatabase(UserDao user, SystemDao systemDao, EntityManager entityManager) throws Exception {
        entityManager.createQuery("DELETE FROM AuthDao WHERE fkUserId = :fkUserId AND fkSystemId = :fkSystemId")
                .setParameter("fkUserId", user.getId())
                .setParameter("fkSystemId", systemDao.getId())
                .executeUpdate();

    }

    private class TokenRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
