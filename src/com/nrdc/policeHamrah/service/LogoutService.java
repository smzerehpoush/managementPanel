package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.LogoutImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogout;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//done
@Path("/logout")
public class LogoutService {
    private static Logger logger = Logger.getLogger(LogoutService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(EncryptedRequest encryptedRequest) {
        logger.info("++================== logout SERVICE : START ==================++");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RequestLogout requestLogout = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestLogout.class);
            StandardResponse response = new LogoutImpl().logout(encryptedRequest.getToken(), requestLogout);
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
