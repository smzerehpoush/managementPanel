package com.nrdc.adminPanel.service.VT;

import com.nrdc.adminPanel.impl.VT.GetVTUsersImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUsers;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getUsers")
public class GetVTUsersService   {
    private static Logger logger = Logger.getLogger(GetVTUsersService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUsersList(RequestGetUsers requestGetUsers) {
        logger.info("++================== getUsers SERVICE : START ==================++");
        try {
            VTResponse<ResponseGetUsers> response = new GetVTUsersImpl().getUsersList(requestGetUsers);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getUsers SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseGetUsers> response = new VTResponse<ResponseGetUsers>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
