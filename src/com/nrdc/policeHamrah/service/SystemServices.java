package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.impl.SystemImpl;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystemWithVersions;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/system")
public class SystemServices {
    private static Logger logger = Logger.getLogger(SystemServices.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getSystemVersions(@QueryParam("token") String token) {
//        logger.info("++================== getRoles SERVICE : START ==================++");
//        try {
//            StandardResponse response = new SystemImpl().getUserSystems(token);
//            String key = UserDao.getKey(token).getKey();
//            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
//            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
//            logger.info("++================== getRoles SERVICE : END ==================++");
//            return finalResponse;
//        } catch (Exception ex) {
//            logger.error("++================== getRoles SERVICE : EXCEPTION ==================++");
//            StandardResponse response = StandardResponse.getNOKExceptions(ex);
//            return Response.status(200).entity(response).build();
//        }
//    }

    @Path("/versions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystems(@QueryParam("token") String token) {
        logger.info("++================== getSystemVersions SERVICE : START ==================++");
        try {
            StandardResponse<ResponseGetSystemWithVersions> response = new SystemImpl().getSystemVersions(token);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getSystemVersions SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getSystemVersions SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }

}
