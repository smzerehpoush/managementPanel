package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseGetSystems;
import com.nrdc.managementPanel.model.dao.System;
import com.nrdc.managementPanel.model.dao.User;

import javax.persistence.EntityManager;
import java.util.List;

public class SystemImpl {
    public StandardResponse getSystems(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            User user = User.validate(token);
            user.checkPrivilege(PrivilegeNames.GET_SYSTEMS);
            List systems = entityManager.createQuery("SELECT s FROM System s")
                    .getResultList();
            ResponseGetSystems responseGetSystems = new ResponseGetSystems();
            responseGetSystems.setSystems(systems);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetSystems);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
    public StandardResponse<ResponseGetSystems> getSystems(Long fkUserId) {
        EntityManager entityManager = Database.getEntityManager();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT s FROM Systems s ");
            if (fkUserId != null) {
                stringBuilder.append("JOIN UserSystem u on s.id = u.fkSystemId WHERE u.fkUserId = ");
                stringBuilder.append(fkUserId);
            }
            String query = stringBuilder.toString();
            List<System> systems = entityManager.createQuery(query).getResultList();
            ResponseGetSystems responseGetSystems = new ResponseGetSystems();
            responseGetSystems.setSystems(systems);
            StandardResponse<ResponseGetSystems> response = new StandardResponse<>();
            response.setResponse(responseGetSystems);
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;
        } catch (Exception ex) {
            return new StandardResponse<ResponseGetSystems>().getNOKExceptions(ex);
        }finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
