package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.PersianCalender;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseVerify;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/verify")
public class VerifyService {
    private static Logger logger = Logger.getLogger(VerifyService.class.getName());

    /**
     * @return simple JSON data to handle state
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyRESTService() {
        logger.info("++================== VERIFY SERVICE : START ==================++");

        ResponseVerify response = new ResponseVerify();
        response.setName(SystemNames.POLICE_HAMRAH.name());
        response.setStatusCode(200);
        response.setStatusMessage("OK");
        response.setTime(PersianCalender.getDate() + " - " + PersianCalender.getTime());
        response.setDeployDate("1397/10/05");
        Response finalResponse = Response.status(200).entity(response).build();
        logger.info("++================== VERIFY SERVICE : END ==================++");
        return finalResponse;
    }

}