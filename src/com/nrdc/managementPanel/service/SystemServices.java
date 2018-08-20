package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.impl.RoleImpl;
import com.nrdc.managementPanel.impl.SystemImpl;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/system")
public class SystemServices {
    private static Logger logger = Logger.getLogger(SystemServices.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(@QueryParam("token")String token) {
        logger.info("++================== getRoles SERVICE : START ==================++");
        try {
            StandardResponse response = new SystemImpl().getSystems(token);
            String key = Database.getUserKey(token).getKey();
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
}
