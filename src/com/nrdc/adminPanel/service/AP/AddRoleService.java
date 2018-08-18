package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.AddRoleImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestAddRole;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAddRole;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addRole")
public class AddRoleService   {
    private static Logger logger = Logger.getLogger(AddRoleService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRole(RequestAddRole requestAddRole) {
        logger.info("++================== add user SERVICE : START ==================++");
        try {
            logger.info(requestAddRole);
            VTResponse<ResponseAddRole> response = new AddRoleImpl().addRole(requestAddRole);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== add user SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== add user SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseAddRole> response = new VTResponse<ResponseAddRole>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
