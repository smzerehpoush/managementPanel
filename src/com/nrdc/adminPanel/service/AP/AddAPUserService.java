package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.AddAPUserImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestAddAPUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAddUser;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/addAPUser")
public class AddAPUserService   {
    private static Logger logger = Logger.getLogger(AddAPUserService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(RequestAddAPUser requestAddUser) {
        logger.info("++================== add user SERVICE : START ==================++");
        try {
            logger.info(requestAddUser);
            VTResponse<ResponseAddUser> response = new AddAPUserImpl().addUser(requestAddUser);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== add user SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== add user SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseAddUser> response = new VTResponse<ResponseAddUser>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
