package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.impl.UserImpl;
import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestActiveUser;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestAddUser;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();



    @Path("/activate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response activeUser(EncryptedRequest encryptedRequest){
        logger.info("++================== login SERVICE : START ==================++");
        try {
            RequestActiveUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestActiveUser.class);
            StandardResponse response = new UserImpl().activeUser(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== login SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== login SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(EncryptedRequest encryptedRequest){
        logger.info("++================== login SERVICE : START ==================++");
        try {
            RequestAddUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestAddUser.class);
            StandardResponse response = new UserImpl().addUser(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== login SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== login SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
}
