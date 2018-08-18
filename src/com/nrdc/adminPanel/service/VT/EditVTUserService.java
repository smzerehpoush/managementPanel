package com.nrdc.adminPanel.service.VT;

import com.nrdc.adminPanel.impl.VT.EditVTUserImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestEditVTUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseEditUser;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/editVTUser")
public class EditVTUserService   {
    private static Logger logger = Logger.getLogger(EditVTUserService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(RequestEditVTUser requestEditUser) {
        logger.info(requestEditUser);
        logger.info("++================== editUser SERVICE : START ==================++");
        try {
            VTResponse<ResponseEditUser> response = new EditVTUserImpl().editUser(requestEditUser);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== editUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== editUser SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseEditUser> response = new VTResponse<ResponseEditUser>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
