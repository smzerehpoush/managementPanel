package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.CustomUser;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestSearchUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseSearchUsers;
import com.nrdc.adminPanel.model.VTUser;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

public class SearchVTUsersImpl {

    public VTResponse<ResponseSearchUsers> searchUsers(RequestSearchUsers requestSearchUsers) {
        EntityManager entityManager = DBManager.getEntityManager();
        try {
            String token = requestSearchUsers.getToken();
            String text = requestSearchUsers.getText();
            Database.adminTokenValidation(token);
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery("SELECT u FROM VTUser u WHERE u.isActive = true AND u.policeCode LIKE '" + text + "%'");
            List<VTUser> VTUsers = query.getResultList();
            List<CustomUser> customUsers = new LinkedList<>();
            for (VTUser VTUser : VTUsers) {
                customUsers.add(new CustomUser(VTUser));
            }
            ResponseSearchUsers responseSearchUsers = new ResponseSearchUsers();
            responseSearchUsers.setUsers(customUsers);
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
