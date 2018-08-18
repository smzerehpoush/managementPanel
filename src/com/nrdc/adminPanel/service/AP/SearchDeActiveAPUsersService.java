package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.SearchDeActiveAPUsersImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestSearchDeActiveUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseSearchDeActiveUsers;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseSearchUsers;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/searchDeActiveAPUsers")
public class SearchDeActiveAPUsersService   {
    private static Logger logger = Logger.getLogger(SearchDeActiveAPUsersService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchUsers(RequestSearchDeActiveUsers requestSearchDeActiveUsers) {
        logger.info("++================== searchDeActiveUsers SERVICE : START ==================++");
        try {
            logger.info(requestSearchDeActiveUsers);
            VTResponse<ResponseSearchDeActiveUsers> responseSearchUsers = new SearchDeActiveAPUsersImpl().searchUsers(requestSearchDeActiveUsers);
            Response finalResponse = Response.status(200).entity(responseSearchUsers).build();
            logger.info("++================== searchDeActiveUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== searchDeActiveUsers SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseSearchUsers> response = new VTResponse<ResponseSearchUsers>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
