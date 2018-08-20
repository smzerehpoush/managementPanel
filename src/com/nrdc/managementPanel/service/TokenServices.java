package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.impl.TokenImpl;
import com.nrdc.managementPanel.impl.UserImpl;
import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestEditUser;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestRemoveToken;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/token")
public class TokenServices {
    private static Logger logger = Logger.getLogger(TokenServices.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();

    @Path("/remove")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(EncryptedRequest encryptedRequest){
        logger.info("++================== editUser SERVICE : START ==================++");
        try {
            RequestRemoveToken request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestRemoveToken.class);
            StandardResponse response = new TokenImpl().removeToken(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== editUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== editUser SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
}
