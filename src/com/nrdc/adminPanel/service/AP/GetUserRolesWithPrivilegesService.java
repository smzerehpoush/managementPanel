package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.GetUserRolesWithPrivilegesImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetUserRolesWithPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUserPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUserRolesWithPrivileges;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getUserRolesWithPrivileges")
public class GetUserRolesWithPrivilegesService   {
    private static Logger logger = Logger.getLogger(GetUserRolesWithPrivilegesService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserRolesWithPrivileges(RequestGetUserRolesWithPrivileges requestGetUserRolesWithPrivileges) {
        logger.info("++================== getUserRolesWithPrivileges SERVICE : START ==================++");
        try {
            logger.info(requestGetUserRolesWithPrivileges);
            VTResponse<ResponseGetUserRolesWithPrivileges> response = new GetUserRolesWithPrivilegesImpl().getUserRolesWithPrivileges(requestGetUserRolesWithPrivileges);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getUserRolesWithPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getUserRolesWithPrivileges SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseGetUserPrivileges> response = new VTResponse<ResponseGetUserPrivileges>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
