package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestSearchUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseSearchUsers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class SearchAPUsersImpl {
    private EntityManager entityManager = DBManager.getEntityManager();

    public VTResponse<ResponseSearchUsers> searchUsers(RequestSearchUsers requestSearchUsers) {
        try {
            String token = requestSearchUsers.getToken();
            String text = requestSearchUsers.getText();
            Database.adminTokenValidation(token);
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery("SELECT u FROM APUser u WHERE u.isActive = true AND u.username LIKE '" + text + "%'");
            List users = query.getResultList();
            ResponseSearchUsers responseSearchUsers = new ResponseSearchUsers();
            responseSearchUsers.setUsers(users);
            VTResponse<ResponseSearchUsers> vtResponse = new VTResponse<>();
            vtResponse.setResultCode(1);
            vtResponse.setResultMessage("OK");
            vtResponse.setResponse(responseSearchUsers);
            return vtResponse;
        } catch (Exception ex) {
            return new VTResponse<ResponseSearchUsers>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
