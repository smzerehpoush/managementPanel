package com.nrdc.policeHamrah.service;

import com.google.gson.Gson;
import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.ABISImpl;
import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/abis")
public class ABISService {
    private static Logger logger = Logger.getLogger(ABISService.class.getName());

    @Path("/{serviceName}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ABISService(EncryptedRequest encryptedRequest, @PathParam("serviceName") String serviceName) {
        logger.info("++================== ABIS SERVICE : START ==================++");
        try {
            Object request = new Gson().fromJson(Encryption.decryptRequest(encryptedRequest), Object.class);
            StandardResponse response = new ABISImpl().sendRequest(request, serviceName);
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(response);
            Response finalResponse = Response.status(Response.Status.OK).entity(encryptedResponse).build();
            logger.info("++================== ABIS SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ServerException.create("++================== ABIS SERVICE : EXCEPTION ==================++", ex);
        }
    }
}
