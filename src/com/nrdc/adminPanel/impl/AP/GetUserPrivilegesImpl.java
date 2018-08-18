package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.exceptions.NotValidTokenException;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetUserPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUserPrivileges;
import com.nrdc.adminPanel.model.VTPrivilege;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.*;

public class GetUserPrivilegesImpl {
    private static Logger logger = Logger.getLogger(GetUserPrivilegesImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();


    public VTResponse<ResponseGetUserPrivileges> getUserPrivileges(RequestGetUserPrivileges requestGetUserPrivileges) throws NotValidTokenException {
        try {
            if (transaction != null && !transaction.isActive()) {
                transaction.begin();
                entityManager.getEntityManagerFactory().getCache().evictAll();
            }
            String token = requestGetUserPrivileges.getToken();
            logger.info("token : " + token);
            Database.adminTokenValidation(token);
            Long fkUserId = requestGetUserPrivileges.getFkUserId();
            logger.info("fkUserId : " + fkUserId);
            Set<VTPrivilege> privileges = getUserPrivileges(fkUserId);
            VTResponse<ResponseGetUserPrivileges> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            ResponseGetUserPrivileges responseUserPrivileges = new ResponseGetUserPrivileges();
            responseUserPrivileges.setPrivileges(privileges);
            response.setResponse(responseUserPrivileges);
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    Set<VTPrivilege> getUserPrivilegesWithoutTokenValidation(Long fkUserId) {
        return getUserPrivileges(fkUserId);
    }

    private List<Long> getUserFkRoleIdList(Long fkUserId) {
        Query query = entityManager.createQuery("SELECT ur.fkRoleId FROM VTUserRole ur WHERE ur.fkUserId = " + fkUserId);
        return (List<Long>) query.getResultList();
    }

    private List<Long> getRoleFkPrivilegeIdList(Long fkRoleId) {
        Query query = entityManager.createQuery("SELECT rp.fkPrivilegeId FROM RolePrivilege rp WHERE rp.fkRoleId = " + fkRoleId);
        return (List<Long>) query.getResultList();
    }

    private VTPrivilege getPrivilegeByFkId(Long fkPrivilegeId) {
        Query query = entityManager.createQuery("SELECT p FROM VTPrivilege p WHERE p.id= " + fkPrivilegeId);
        return (VTPrivilege) query.getSingleResult();
    }

    private List<VTPrivilege> getUserPrivilegesList(Long fkUserId) {
        List<Long> fkRoleIdList = getUserFkRoleIdList(fkUserId);
        List<Long> fkPrivilegeIdList = new ArrayList<>();
        List<VTPrivilege> privileges = new LinkedList<>();
        for (Long fkRoleId : fkRoleIdList) {
            fkPrivilegeIdList.addAll(getRoleFkPrivilegeIdList(fkRoleId));
        }
        for (Long fkPrivilegeId : fkPrivilegeIdList) {
            privileges.add(getPrivilegeByFkId(fkPrivilegeId));
        }
        return privileges;
    }

    private Set<VTPrivilege> getUserPrivileges(Long fkUserId) {
        List<VTPrivilege> userPrivileges = getUserPrivilegesList(fkUserId);
        Set<VTPrivilege> vtPrivileges = new HashSet<>();
        vtPrivileges.addAll(userPrivileges);
        return vtPrivileges;
    }
}
