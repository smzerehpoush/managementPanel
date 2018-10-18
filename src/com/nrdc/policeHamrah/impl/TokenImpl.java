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

public class TokenImpl {
    public StandardResponse removeToken(String token, RequestRemoveToken requestRemoveToken) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            UserDao user = UserDao.validate(token);
            if (user.getId().equals(requestRemoveToken.getFkUserId())) {
                throw new Exception(Constants.CAN_NOT_DELETE_TOKEN);
            }
            SystemDao systemOfRemovingToken = SystemDao.getSystem(requestRemoveToken.getFkSystemId());
            // TODO: 2018-10-18 باید به نحوی باشد که برود سیستم های کاربر را چک کند و درصورتیکه جزو سیستم ها بود حذف کند
            PrivilegeDao privilege = PrivilegeDao.getPrivilege(PrivilegeNames.REMOVE_TOKEN, systemOfRemovingToken.getId());
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

}
