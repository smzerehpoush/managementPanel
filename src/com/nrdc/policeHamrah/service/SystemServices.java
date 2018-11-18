package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.SystemImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystemWithVersions;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//done
@Path("/system")
public class SystemServices {
    private static Logger logger = Logger.getLogger(SystemServices.class.getName());

    /**
     * 09
     * list of systems with last version of them
     * @param token user token
     * @return ResponseGetSystemWithVersions list of system with their last versions
     */
    @Path("/versions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemWithVersion(@QueryParam("token") String token) {
        logger.info("++================== getSystemWithVersion SERVICE : START ==================++");
        try {
            if (token == null ) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetSystemWithVersions> response = new SystemImpl().getSystemVersions(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getSystemWithVersion SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getSystemWithVersion SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


    /**
     * 10
     * list of users a system
     * @param token      user token
     * @param fkSystemId is of system
     * @return ResponseGetUsers : list of users of a system
     */
    @Path("/users")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemUsers(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getSystemUsers SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetUsers> response = new SystemImpl().getSystemUsers(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getSystemUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getSystemUsers SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * 23
     * list of users a system
     * @param token      user token
     * @param fkSystemId is of system
     * @return ResponseGetUsers : list of users of a system
     */
    @Path("/roles")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemRoles(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getSystemRoles SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetRoles> response = new SystemImpl().getSystemRoles(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getSystemRoles SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getSystemRoles SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
}
