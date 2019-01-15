package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.model.dao.PrivilegeDao;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import com.nrdc.policeHamrah.model.dao.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class TokenImpl {

    public StandardResponse removeToken(String token, Long fkSystemId, Long fkUserId) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user = UserDao.validate(token);
            String dbToken;
            try {
                dbToken = (String) entityManager.createQuery("SELECT a.token FROM AuthDao a WHERE a.fkUserId = :fkUserId AND a.fkSystemId = :fkSystemId")
                        .setParameter("fkUserId", fkUserId)
                        .setParameter("fkSystemId", fkSystemId)
                        .getSingleResult();
            } catch (Exception ex) {
                throw new Exception(Constants.NOT_LOGIN_ED);
            }
            if (token.equals(dbToken))
                throw new Exception(Constants.CAN_NOT_DELETE_TOKEN);
            SystemDao inputSystem = SystemDao.getSystem(fkSystemId);
            List<SystemDao> userSystems = user.systems();
            checkUserSystems(inputSystem, userSystems);
            PrivilegeDao privilege = PrivilegeDao.getPrivilege(PrivilegeNames.REMOVE_TOKEN);
            user.checkPrivilege(privilege, fkSystemId);
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("DELETE FROM AuthDao t WHERE t.fkUserId = :fkUserId AND t.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", fkUserId)
                    .setParameter("fkSystemId", fkSystemId)
                    .executeUpdate();
            new LogoutImpl().deleteAuthInfoFromAnotherSystemDatabase(dbToken, inputSystem);
            if (transaction.isActive())
                transaction.commit();


            return new StandardResponse<>();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkUserSystems(SystemDao inputSystem, List<SystemDao> userSystems) throws Exception {
        for (SystemDao system : userSystems) {
            if (system.getId().equals(inputSystem.getId()))
                return;
        }
        throw new Exception(Constants.USER_SYSTEM_ERROR);
    }

}
