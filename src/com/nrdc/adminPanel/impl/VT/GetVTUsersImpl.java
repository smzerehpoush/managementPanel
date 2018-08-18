package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.CustomUser;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.TokenRequest;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.adminPanel.model.VTUser;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

public class GetVTUsersImpl {

    public VTResponse<ResponseGetUsers> getUsersList(TokenRequest request) throws Exception {
        EntityManager entityManager = DBManager.getEntityManager();
        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);
            entityManager.getEntityManagerFactory().getCache().evictAll();

            Query query = entityManager.createQuery("SELECT u FROM VTUser u WHERE u.isActive = true");
            List<VTUser> result = query.getResultList();
            List<CustomUser> finalResult = new LinkedList<>();
            for (VTUser VTUser : result) {
                finalResult.add(new CustomUser(VTUser));
            }
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(finalResult);
            VTResponse<ResponseGetUsers> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetUsers);
            return response;
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }
}
