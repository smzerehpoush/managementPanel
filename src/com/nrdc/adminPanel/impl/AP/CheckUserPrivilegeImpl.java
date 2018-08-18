package com.nrdc.adminPanel.impl.AP;

import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestCheckUserPrivilege;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseCheckUserPrivilege;
import com.nrdc.adminPanel.model.VTPrivilege;
import org.apache.log4j.Logger;

import java.util.Set;

public class CheckUserPrivilegeImpl {
    private static Logger logger = Logger.getLogger(CheckUserPrivilegeImpl.class.getName());

    public VTResponse<ResponseCheckUserPrivilege> checkUserPrivilege(RequestCheckUserPrivilege requestCheckUserPrivilege) {
        Long fkUserId = requestCheckUserPrivilege.getFkUserId();
        Long fkPrivilegeId = requestCheckUserPrivilege.getFkPrivilegeId();
        logger.info("fkUserId : " + fkUserId);
        logger.info("fkPrivilegeId : " + fkPrivilegeId);
        boolean result = checkUserPrivilegeImpl(fkUserId, fkPrivilegeId);
        logger.info("result : " + result);
        VTResponse<ResponseCheckUserPrivilege> response = new VTResponse<>();
        response.setResultCode(1);
        response.setResultMessage("OK");
        return response;
    }

    public boolean checkUserPrivilegeImpl(Long fkUserId, Long fkPrivilegeId) {
        Set<VTPrivilege> privileges = new GetUserPrivilegesImpl().getUserPrivilegesWithoutTokenValidation(fkUserId);
        for (VTPrivilege privilege : privileges) {
            if (privilege.getId().equals(fkPrivilegeId))
                return true;
        }
        return false;
    }


}
