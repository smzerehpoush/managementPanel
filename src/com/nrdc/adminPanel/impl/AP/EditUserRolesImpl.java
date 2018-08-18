package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestEditUserRoles;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseEditUserRoles;
import com.nrdc.adminPanel.model.VTUserRole;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class EditUserRolesImpl {
    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTResponse<ResponseEditUserRoles> editUserRoles(RequestEditUserRoles requestEditUserRoles) {
        String token = requestEditUserRoles.getToken();
        try {

            Database.adminTokenValidation(token);
            Long fkUserId = requestEditUserRoles.getFkUserId();
            checkUser(token, fkUserId);
//            List<Long> dbRoleIdList = entityManager.createQuery("SELECT ur.fkRoleId FROM VTUserRole ur WHERE ur.fkUserId =" + fkUserId)
//                    .getResultList();
//            for (Long dbRoleId : dbRoleIdList) {
//                if (!requestEditUserRoles.getFkRoleIdList().contains(dbRoleId))
//                    entityManager.createQuery("DELETE FROM VTUserRole WHERE fkUserId = :fkUserId AND fkRoleId = :dbRoleId")
//                            .setParameter("fkUserId", fkUserId)
//                            .setParameter("dbRoleId", dbRoleId)
//                            .executeUpdate();
//            }
//            for (Long roleId : requestEditUserRoles.getFkRoleIdList()) {
//                if (isValidRole(roleId) && !dbRoleIdList.contains(roleId)) {
//                    VTUserRole userRole = new VTUserRole();
//                    userRole.setFkUserId(fkUserId);
//                    userRole.setFkRoleId(roleId);
//                    entityManager.persist(userRole);
//                }
//            }
            entityManager.createQuery("DELETE FROM VTUserRole WHERE fkUserId = :fkUserId")
                    .setParameter("fkUserId", fkUserId)
                    .executeUpdate();
            for (Long roleId : requestEditUserRoles.getFkRoleIdList()) {
                if (isValidRole(roleId)) {
                    VTUserRole userRole = new VTUserRole();
                    userRole.setFkUserId(fkUserId);
                    userRole.setFkRoleId(roleId);
                    entityManager.persist(userRole);
                }
            }
            VTResponse<ResponseEditUserRoles> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            if (transaction != null && transaction.isActive())
                transaction.commit();
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return new VTResponse<ResponseEditUserRoles>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    private boolean isValidRole(Long roleId) {
        int size = entityManager.createQuery("SELECT r FROM VTRole r WHERE r.id = :id").setParameter("id", roleId).getResultList().size();
        return (size == 1);
    }

    private void checkUser(String token, Long userId) throws Exception {

        Long fkUserId = Database.getFkUserIdByAdminToken(token);
        if (fkUserId.equals(userId)) {
            throw new Exception(Constants.CAN_NOT_CHANGE_YOUR_ROLE);
        }
        if (transaction != null && !transaction.isActive())
            transaction.begin();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        int size = entityManager.createQuery("SELECT u FROM APUser u WHERE u.id = :id")
                .setParameter("id", userId)
                .getResultList()
                .size();
        if (size != 1)
            throw new Exception(Constants.USER_IS_NOT_VALID);
    }


}
