package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.SystemImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetPrivileges;
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
     * @param token user token
     * @return ResponseGetSystemWithVersions list of system with their last versions
     */
    @Path("/versions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemWithVersion(@QueryParam("token") String token) {
        logger.info("++================== getSystemWithVersion SERVICE : START ==================++");
        try {
            StandardResponse<ResponseGetSystemWithVersions> response = new SystemImpl().getSystemVersions(token);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getSystemWithVersion SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getSystemWithVersion SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * @param token      user token
     * @param fkSystemId id of system
     * @return ResponseGetPrivileges list of privileges of a system
     */
    @Path("/privileges")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemPrivileges(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getSystemWithVersion SERVICE : START ==================++");
        try {
            StandardResponse<ResponseGetPrivileges> response = new SystemImpl().getSystemPrivileges(token, fkSystemId);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getSystemWithVersion SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getSystemWithVersion SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }

    /**
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

}
