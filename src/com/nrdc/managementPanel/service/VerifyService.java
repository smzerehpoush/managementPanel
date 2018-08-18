package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseVerify;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("/verify")
public class VerifyService {
    private static Logger logger = Logger.getLogger(VerifyService.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response verifyRESTService() {
        logger.info("++================== VERIFY SERVICE : START ==================++");

        ResponseVerify response = new ResponseVerify();
        response.setName("Management Panel");
        response.setStatus("OK");
        response.setStatusCode(200);
        response.setTime(new Date().toString());
        Response finalResponse = Response.status(200).entity(response).build();
        logger.info("++================== VERIFY SERVICE : END ==================++");
        return finalResponse;
    }

}
