package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.policeHamrah.model.dao.UserDao;

import javax.persistence.EntityManager;
import java.util.List;

public class PrivilegeImpl {
    public StandardResponse<ResponseGetPrivileges> getPrivileges(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List privileges = entityManager.createQuery("SELECT distinct (p) FROM PrivilegeDao p " +
                    "JOIN RolePrivilegeDao rp ON p.id = rp.fkPrivilegeId " +
                    "JOIN UserRoleDao ur ON rp.fkRoleId = ur.fkRoleId " +
                    "JOIN AuthDao t ON ur.fkUserId = t.fkUserId " +
                    "JOIN RoleDao r ON r.id = rp.fkRoleId " +
                    "WHERE r.fkSystemId = :fkSystemId AND t.token= :token")
                    .setParameter("token", token)
                    .setParameter("fkSystemId", fkSystemId)
                    .getResultList();
            ResponseGetPrivileges responseGetPrivileges = new ResponseGetPrivileges();
            responseGetPrivileges.setPrivileges(privileges);
            StandardResponse<ResponseGetPrivileges> response = new StandardResponse<>();
            response.setResponse(responseGetPrivileges);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

}
