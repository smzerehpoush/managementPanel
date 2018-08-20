package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.impl.RoleImpl;
import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestGetRolePrivileges;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/role")
public class RoleServices {
    private static Logger logger = Logger.getLogger(UserServices.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(EncryptedRequest encryptedRequest) {
        logger.info("++================== getRoles SERVICE : START ==================++");
        try {
            StandardResponse response = new RoleImpl().getRoles(encryptedRequest.getToken());
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getRoles SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getRoles SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    @Path("/privileges")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivileges(EncryptedRequest encryptedRequest) {
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
            RequestGetRolePrivileges request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestGetRolePrivileges.class);
            StandardResponse response = new RoleImpl().getPrivileges(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getPrivileges SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();

        }
    }
}

