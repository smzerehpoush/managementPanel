package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.EditUserRolesImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestEditUserRoles;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseEditUserRoles;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/editUserRoles")
public class EditUserRolesService   {
    private static Logger logger = Logger.getLogger(EditUserRolesService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUserRoles(RequestEditUserRoles requestEditUserRoles) {
        logger.info("++================== editUserRoles SERVICE : START ==================++");
        try {
            logger.info(requestEditUserRoles);
            VTResponse<ResponseEditUserRoles> response = new EditUserRolesImpl().editUserRoles(requestEditUserRoles);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== editUserRoles SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== editUserRoles SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseEditUserRoles> response = new VTResponse<ResponseEditUserRoles>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
