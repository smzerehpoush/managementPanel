package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.TokenRequest;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetDeActiveUsers;
import com.nrdc.adminPanel.model.VTUser;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class GetDeActiveVTUsersImpl {

    public VTResponse<ResponseGetDeActiveUsers> getDeActiveUsersList(TokenRequest request) throws Exception {
        EntityManager entityManager = DBManager.getEntityManager();
        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);
            Query query = entityManager.createQuery("SELECT u FROM VTUser u WHERE u.isActive = false ");
            List<VTUser> result = query.getResultList();
            ResponseGetDeActiveUsers responseGetDeActiveUsers = new ResponseGetDeActiveUsers();
            responseGetDeActiveUsers.setUsers(result);
            VTResponse<ResponseGetDeActiveUsers> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetDeActiveUsers);
            return response;
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
