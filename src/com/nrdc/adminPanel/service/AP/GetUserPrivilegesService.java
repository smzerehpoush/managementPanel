package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.GetUserPrivilegesImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetUserPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUserPrivileges;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getUserPrivileges")
public class GetUserPrivilegesService extends SimpleService {
    private static Logger logger = Logger.getLogger(GetUserPrivilegesService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserPrivileges(RequestGetUserPrivileges requestUserPrivileges) {
        logger.info("++================== getUserPrivileges SERVICE : START ==================++");
        try {
            logger.info(requestUserPrivileges);
            VTResponse<ResponseGetUserPrivileges> response = new GetUserPrivilegesImpl().getUserPrivileges(requestUserPrivileges);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getUserPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getUserPrivileges SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseGetUserPrivileges> response = new VTResponse<ResponseGetUserPrivileges>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
