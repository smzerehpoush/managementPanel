package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.exceptions.ExceptionHandler;
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
    public Response getAllSystem(@QueryParam("token") String token) {
        logger.info("++================== getAllSystems SERVICE : START ==================++");
        try {
            if (token == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getAllSystems(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getAllSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getAllSystems SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 34
     * list of systems with last version of them
     *
     * @return list of all systems
     */
    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSystem() {
        logger.info("++================== getAllSystems SERVICE : START ==================++");
        try {
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getAllSystems();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getAllSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getAllSystems SERVICE : EXCEPTION ==================++", ex, Constants.DEFAULT_KEY);
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
    public Response getSystemWithVersion(@QueryParam("token") String token) {
        logger.info("++================== getSystemWithVersion SERVICE : START ==================++");
        try {
            if (token == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse<ResponseGetSystemWithVersions> response = new SystemImpl().getSystemVersions(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getSystemWithVersion SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getSystemWithVersion SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /***
     * 35
     * return list of systems in tabular format
     * @return tabular fromat of systems
     */
    @Path("/version")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getSystem() {
        logger.info("++================== getSystemWithVersion SERVICE : START ==================++");
        try {
            String response = new SystemImpl().getSystem();
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getSystemWithVersion SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * 36
     * get last version of given system
     *
     * @param fkSystemId id of system
     * @return json data
     */
    @Path("/version/{fkSystemId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystem(@PathParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getSystemWithVersion SERVICE : START ==================++");
        try {
            String result = new SystemImpl().getSystem(fkSystemId);
//            String result = "http://localhost:9002/apk/ph-v8.apk";
            EncryptedResponse encryptedResponse = new EncryptedResponse();
            encryptedResponse.setData(result);
            Response response = Response.status(200).entity(encryptedResponse)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Headers",
                            "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Methods", "GET")
                    .build();

            logger.info("++================== getSystemWithVersion SERVICE : END ==================++");
            return response;
        } catch (Exception ex) {
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
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
    public Response getSystemUsers(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getSystemUsers SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse<ResponseGetUsers> response = new SystemImpl().getSystemUsers(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getSystemUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getSystemUsers SERVICE : EXCEPTION ==================++", ex, token);
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
    public Response getSystemRoles(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getSystemRoles SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse<ResponseGetRoles> response = new SystemImpl().getSystemRoles(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getSystemRoles SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== gesSystemRoles SERVICE : EXCEPTION ==================++", ex, token);
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
    public Response getSystemRolesWithPrivileges(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getUserRolesWithPrivileges SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse<ResponseGetRolesWithPrivileges> response = new SystemImpl().getSystemRolesWithPrivileges(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUserRolesWithPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getUserRolesWithPrivileges SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 30
     * reportSystem rate and other info of system
     *
     * @param encryptedRequest RequestResetPassword
     * @return simple StandardResponse to handle state
     */
    @Path("/report")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportSystem(EncryptedRequest encryptedRequest) {
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
            return ExceptionHandler.create("++================== reportSystem SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }

    /**
     * 29
     * report download of a specific app
     *
     * @param token       user token
     * @param fkSystemId  id of system
     * @param versionCode version of application
     * @return simple standard response to handle state
     */
    @Path("/download")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response incrementDownloadCount(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId, @QueryParam("versionCode") Long versionCode) {
        logger.info("++================== incrementDownloadCount SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null || versionCode == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse response = new SystemImpl().incrementDownloadCount(token, fkSystemId, versionCode);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== incrementDownloadCount SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== incrementDownloadCount SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 31
     * get reports of a system sorted by time
     *
     * @param token      user token
     * @param fkSystemId id of system
     * @return simple standard response to handle state
     */
    @Path("/report")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemReports(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== incrementDownloadCount SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse response = new SystemImpl().getSystemReports(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== incrementDownloadCount SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== incrementDownloadCount SERVICE : EXCEPTION ==================++", ex, token);
        }
    }
}
