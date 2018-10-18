package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.impl.LogoutImpl;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogout;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/logout")
public class LogoutService {
    private static Logger logger = Logger.getLogger(LogoutService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(RequestLogout requestLogout) {
        logger.info("++================== logout SERVICE : START ==================++");
        try {
            StandardResponse response = new LogoutImpl().logout(requestLogout);
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
