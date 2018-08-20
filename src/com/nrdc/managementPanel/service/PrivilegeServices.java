package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.impl.PrivilegeImpl;
import com.nrdc.managementPanel.impl.UserImpl;
import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestGetUsers;
import com.nrdc.managementPanel.model.Token;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/privilege")
public class PrivilegeServices {
    private static Logger logger = Logger.getLogger(UserServices.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivileges(EncryptedRequest encryptedRequest){
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
            StandardResponse response = new PrivilegeImpl().getPrivileges(encryptedRequest.getToken());
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
