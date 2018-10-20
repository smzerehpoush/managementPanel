package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestRemoveToken;
import com.nrdc.policeHamrah.model.dao.PrivilegeDao;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import com.nrdc.policeHamrah.model.dao.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class TokenImpl {

    public StandardResponse removeToken(String token, RequestRemoveToken requestRemoveToken) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            UserDao user = UserDao.validate(token);
            if (user.getId().equals(requestRemoveToken.getFkUserId())) {
                throw new Exception(Constants.CAN_NOT_DELETE_TOKEN);
            }
            SystemDao inputSystem = SystemDao.getSystem(requestRemoveToken.getFkSystemId());
            List<SystemDao> userSystems = user.systems();
            checkUserSystems(inputSystem, userSystems);
            PrivilegeDao privilege = PrivilegeDao.getPrivilege(PrivilegeNames.REMOVE_TOKEN, inputSystem.getId());
            user.checkPrivilege(privilege);

            entityManager.createQuery("DELETE FROM TokenDao t WHERE t.fkUserId = :fkUserId AND t.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", requestRemoveToken.getFkUserId())
                    .setParameter("fkSystemId", requestRemoveToken.getFkSystemId())
                    .executeUpdate();
            entityManager.createQuery("DELETE FROM KeyDao k WHERE k.fkUserId = :fkUserId AND k.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", requestRemoveToken.getFkUserId())
                    .setParameter("fkSystemId", requestRemoveToken.getFkSystemId())
                    .executeUpdate();
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();


            return response;
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
