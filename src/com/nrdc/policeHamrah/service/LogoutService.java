package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.exceptions.ExceptionHandler;
import com.nrdc.policeHamrah.exceptions.ServerException;
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
     * logout from systems with fkSystemId, if fkSystemId is null then logout from PH
     *
     * @param token      token of user that want to logout
     * @param fkSystemId system that user want to logout
     * @return Simple Standard Response to handle state of logout
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== logout SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            logger.info("Request Logout = { token : " + token + " , fkSystemId : " + fkSystemId + " }");
            StandardResponse response = new LogoutImpl().logout(token, fkSystemId);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== logout SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== logout SERVICE : EXCEPTION ==================++", ex, token);
        }
    }
}
