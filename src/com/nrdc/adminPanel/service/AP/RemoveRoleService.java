package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.RemoveRoleImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestRemoveRole;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseRemoveRole;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/removeRole")
public class RemoveRoleService extends SimpleService {
    private static Logger logger = Logger.getLogger(RemoveRoleService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(RequestRemoveRole requestRemoveRole) {
        logger.info("++================== removeRole SERVICE : START ==================++");
        try {
            logger.info(requestRemoveRole);
            VTResponse<ResponseRemoveRole> response = new RemoveRoleImpl().removeRole(requestRemoveRole);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== removeRole SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== removeRole SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseRemoveRole> response = new VTResponse<ResponseRemoveRole>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
