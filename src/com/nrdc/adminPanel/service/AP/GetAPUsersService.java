package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.GetAdminUsersImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetAdminUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseRemoveToken;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getAdminUsers")
public class GetAPUsersService   {
    private static Logger logger = Logger.getLogger(GetAPUsersService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(RequestGetAdminUsers requestGetAdminUsers) {
        logger.info("++================== getAdminUsers SERVICE : START ==================++");
        try {
            logger.info(requestGetAdminUsers);
            VTResponse<ResponseGetUsers> response = new GetAdminUsersImpl().getAdminUsersList(requestGetAdminUsers);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getAdminUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getAdminUsers SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseRemoveToken> response = new VTResponse<ResponseRemoveToken>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
