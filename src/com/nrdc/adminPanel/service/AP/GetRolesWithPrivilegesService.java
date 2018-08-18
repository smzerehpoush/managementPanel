package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.GetRolesWithPrivilegesImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetRolesWithPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetRolesWithPrivileges;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getRolesWithPrivileges")
public class GetRolesWithPrivilegesService extends SimpleService {
    private static Logger logger = Logger.getLogger(GetRolesWithPrivilegesService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response GetRolesWithPrivilegesImpl(RequestGetRolesWithPrivileges requestGetRolesWithPrivileges) {
        logger.info("++================== getRolesWithPrivileges SERVICE : START ==================++");
        try {
            logger.info(requestGetRolesWithPrivileges);
            VTResponse<ResponseGetRolesWithPrivileges> response = new GetRolesWithPrivilegesImpl().getRolesWithPrivileges(requestGetRolesWithPrivileges);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getRolesWithPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getRolesWithPrivileges SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseGetPrivileges> response = new VTResponse<ResponseGetPrivileges>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
