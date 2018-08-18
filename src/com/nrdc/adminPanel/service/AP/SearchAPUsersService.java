package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.SearchAPUsersImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestSearchUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseSearchUsers;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/searchAPUsers")
public class SearchAPUsersService extends SimpleService {
    private static Logger logger = Logger.getLogger(SearchAPUsersService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchUsers(RequestSearchUsers requestSearchUsers) {
        logger.info("++================== searchUsers SERVICE : START ==================++");
        try {
            logger.info(requestSearchUsers);
            VTResponse<ResponseSearchUsers> responseSearchUsers = new SearchAPUsersImpl().searchUsers(requestSearchUsers);
            Response finalResponse = Response.status(200).entity(responseSearchUsers).build();
            logger.info("++================== searchUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== searchUsers SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseSearchUsers> response = new VTResponse<ResponseSearchUsers>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
