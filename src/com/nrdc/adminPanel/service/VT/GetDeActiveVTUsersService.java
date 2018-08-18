package com.nrdc.adminPanel.service.VT;

import com.nrdc.adminPanel.impl.VT.GetDeActiveVTUsersImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetDeActiveUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getDeActiveVTUsers")
public class GetDeActiveVTUsersService extends SimpleService {
    private static Logger logger = Logger.getLogger(GetDeActiveVTUsersService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUsersList(RequestGetUsers requestGetUsers) {
        logger.info("++================== getUsers SERVICE : START ==================++");
        try {
            VTResponse<ResponseGetDeActiveUsers> response = new GetDeActiveVTUsersImpl().getDeActiveUsersList(requestGetUsers);
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
