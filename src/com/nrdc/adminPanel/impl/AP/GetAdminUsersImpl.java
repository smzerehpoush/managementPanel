package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.TokenRequest;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUsers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class GetAdminUsersImpl {
    private EntityManager entityManager = DBManager.getEntityManager();

    public VTResponse<ResponseGetUsers> getAdminUsersList(TokenRequest request) throws Exception {
        try {
            String token = request.getToken();
            Database.adminTokenValidation(token);
            Query query = entityManager.createQuery("SELECT u FROM APUser u WHERE u.isActive = true");
            List result = query.getResultList();
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(result);
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
