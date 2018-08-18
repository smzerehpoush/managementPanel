package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.exceptions.NotValidTokenException;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.adminPanel.model.VTPrivilege;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class GetPrivilegesImpl {
    private static Logger logger = Logger.getLogger(GetPrivilegesImpl.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();


    public VTResponse<ResponseGetPrivileges> getPrivileges(RequestGetPrivileges requestGetPrivileges) throws NotValidTokenException {
        String token = requestGetPrivileges.getToken();
        logger.info("token : " + token);
        Database.adminTokenValidation(token);
        List<VTPrivilege> privileges = getPrivilegesList();
        ResponseGetPrivileges responseGetPrivileges = new ResponseGetPrivileges();
        responseGetPrivileges.setPrivileges(privileges);
        VTResponse<ResponseGetPrivileges> response = new VTResponse<>();
        response.setResultCode(1);
        response.setResultMessage("OK");
        response.setResponse(responseGetPrivileges);
        return response;
    }

    public List<VTPrivilege> getPrivilegesList() {
        Query query = entityManager.createQuery("SELECT p FROM VTPrivilege p");
        return query.getResultList();
    }
}
