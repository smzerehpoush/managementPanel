package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetRoles;
import com.nrdc.policeHamrah.model.dao.UserDao;

import javax.persistence.EntityManager;
import java.util.List;

public class RoleImpl {
    public StandardResponse getRoles(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.GET_ROLES);
            List roles = entityManager.createQuery("SELECT r FROM RoleDao r")
                    .getResultList();
            ResponseGetRoles responseGetRoles = new ResponseGetRoles();
            responseGetRoles.setRoles(roles);
            StandardResponse response = new StandardResponse<>();


            response.setResponse(responseGetRoles);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getPrivileges(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.GET_PRIVILEGES);
            List privileges = entityManager.createQuery("SELECT p FROM PrivilegeDao p JOIN RolePrivilegeDao rp ON p.id = rp.fkPrivilegeId WHERE rp.fkRoleId = :fkRoleId")
                    .setParameter("fkRoleId", user.getId())
                    .getResultList();
            ResponseGetPrivileges responseGetPrivileges = new ResponseGetPrivileges();
            responseGetPrivileges.setPrivileges(privileges);
            StandardResponse response = new StandardResponse<>();


            response.setResponse(responseGetPrivileges);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
