package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestSearchDeActiveUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseSearchDeActiveUsers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class SearchDeActiveAPUsersImpl {

    public VTResponse<ResponseSearchDeActiveUsers> searchUsers(RequestSearchDeActiveUsers requestSearchDeActiveUsers) {
        EntityManager entityManager = DBManager.getEntityManager();
        try {
            String token = requestSearchDeActiveUsers.getToken();
            String text = requestSearchDeActiveUsers.getText();
            Database.adminTokenValidation(token);
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery("SELECT u FROM APUser u WHERE u.isActive = false AND u.username LIKE '" + text + "%'");
            List users = query.getResultList();

            ResponseSearchDeActiveUsers responseSearchDeActiveUsers = new ResponseSearchDeActiveUsers();
            responseSearchDeActiveUsers.setUsers(users);
            VTResponse<ResponseSearchDeActiveUsers> vtResponse = new VTResponse<>();
            vtResponse.setResultCode(1);
            vtResponse.setResultMessage("OK");
            vtResponse.setResponse(responseSearchDeActiveUsers);
            return vtResponse;
        } catch (Exception ex) {
            return new VTResponse<ResponseSearchDeActiveUsers>().getNOKExceptions(ex.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
