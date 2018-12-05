package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.SystemImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestReportSystem;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.*;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//done
@Path("/system")
public class SystemServices {
    private static Logger logger = Logger.getLogger(SystemServices.class.getName());

    /**
     * 27
     * list of systems with last version of them
     *
     * @param token user token
     * @return list of all systems
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSystem(@QueryParam("token") String token) throws Exception {
        logger.info("++================== getAllSystems SERVICE : START ==================++");
        try {
            if (token == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getAllSystems(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getAllSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ServerException.create("++================== getAllSystems SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 09
     * list of systems with last version of them
     *
     * @param token user token
     * @return ResponseGetSystemWithVersions list of system with their last versions
     */
    @Path("/versions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemWithVersion(@QueryParam("token") String token) throws Exception {
        logger.info("++================== getSystemWithVersion SERVICE : START ==================++");
        try {
            if (token == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetSystemWithVersions> response = new SystemImpl().getSystemVersions(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getSystemWithVersion SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ServerException.create("++================== getSystemWithVersion SERVICE : EXCEPTION ==================++", ex, token);
        }
    }


    /**
     * 10
     * list of users a system
     *
     * @param token      user token
     * @param fkSystemId is of system
     * @return ResponseGetUsers : list of users of a system
     */
    @Path("/users")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemUsers(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
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
            return ServerException.create("++================== getSystemUsers SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 23
     * list of users a system
     *
     * @param token      user token
     * @param fkSystemId is of system
     * @return ResponseGetUsers : list of users of a system
     */
    @Path("/roles")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemRoles(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
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
            return ServerException.create("++================== gesSystemRoles SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 24
     * list of roles with privileges of a system
     *
     * @param token      user token
     * @param fkSystemId id of system
     * @return list of roles with privileges of a system
     */
    @Path("/roles/privileges")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemRolesWithPrivileges(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
        logger.info("++================== getUserRolesWithPrivileges SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetRolesWithPrivileges> response = new SystemImpl().getSystemRolesWithPrivileges(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUserRolesWithPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ServerException.create("++================== getUserRolesWithPrivileges SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 29
     * reportSystem rate and other info of system
     *
     * @param encryptedRequest RequestResetPassword
     * @return simple StandardResponse to handle state
     */
    @Path("/reportSystem")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportSystem(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== reportSystem SERVICE : START ==================++");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RequestReportSystem request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestReportSystem.class);
            StandardResponse response = new SystemImpl().reportSystem(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== reportSystem SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ServerException.create("++================== reportSystem SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }

    /**
     * 30
     * list of roles with privileges of a system
     *
     * @param token      user token
     * @param fkSystemId id of system
     * @return list of roles with privileges of a system
     */
    @Path("/download")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response incrementDownloadCount(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId, @QueryParam("versionCode") Long versionCode) throws Exception {
        logger.info("++================== incrementDownloadCount SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null || versionCode == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new SystemImpl().incrementDownloadCount(token, fkSystemId, versionCode);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== incrementDownloadCount SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ServerException.create("++================== incrementDownloadCount SERVICE : EXCEPTION ==================++", ex, token);
        }
    }
}
