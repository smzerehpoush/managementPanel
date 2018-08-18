package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.CheckUserPrivilegeImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestCheckUserPrivilege;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseCheckUserPrivilege;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/checkUserPrivilege")
public class CheckUserPrivilegesService   {
    private static Logger logger = Logger.getLogger(CheckUserPrivilegesService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserPrivileges(RequestCheckUserPrivilege requestCheckUserPrivilege) {
        logger.info(requestCheckUserPrivilege);
        logger.info("++================== checkUserPrivileges SERVICE : START ==================++");
        try {
            VTResponse<ResponseCheckUserPrivilege> responseCheckUserPrivilege = new CheckUserPrivilegeImpl().checkUserPrivilege(requestCheckUserPrivilege);
            Response finalResponse = Response.status(200).entity(responseCheckUserPrivilege).build();
            logger.info("++================== checkUserPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== checkUserPrivileges SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseCheckUserPrivilege> response = new VTResponse<ResponseCheckUserPrivilege>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
