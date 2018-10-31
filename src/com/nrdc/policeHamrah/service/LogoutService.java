package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.impl.LogoutImpl;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//done
@Path("/logout")
public class LogoutService {
    private static Logger logger = Logger.getLogger(LogoutService.class.getName());

    /**
     * 03
     * @param token      token of user that want to logout
     * @param fkSystemId system that user want to logout
     * @return Simple Standard Response to handle state of logout
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== logout SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new LogoutImpl().logout(token, fkSystemId);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== logout SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== logout SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
}
