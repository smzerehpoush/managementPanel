package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.exceptions.ExceptionHandler;
import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.SystemImpl;
import com.nrdc.policeHamrah.impl.UserImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.*;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetRolesWithPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystems;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserServices {
    private static Logger logger = Logger.getLogger(UserServices.class.getName());
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 12
     *
     * @param encryptedRequest RequestAddUser
     * @return simple StandardResponse to handle state
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== addUser SERVICE : START ==================++");
        try {
            RequestAddUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestAddUser.class);
            StandardResponse response = new UserImpl().addUser(encryptedRequest.getToken(), request, true);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== addUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== addUser SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }

    /**
     * 33
     *
     * @param encryptedRequest RequestAddUser
     * @return simple StandardResponse to handle state
     */
    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== addUser SERVICE : START ==================++");
        try {
            RequestAddUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestAddUser.class);
            StandardResponse response = new UserImpl().addUser(encryptedRequest.getToken(), request, false);
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== addUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== addUser SERVICE : EXCEPTION ==================++", ex);
        }
    }

    /**
     * 13
     *
     * @param encryptedRequest RequestEditUser
     * @return simple StandardResponse to handle state
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== editUser SERVICE : START ==================++");
        try {
            RequestEditUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestEditUser.class);
            StandardResponse response = new UserImpl().editUser(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== editUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== editUser SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }

    /**
     * 14
     *
     * @param token      user token
     * @param fkUserId   id of user to be activated
     * @param fkSystemId id of system of changing user
     * @return simple StandardResponse to handle state
     */
    @Path("/active")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response activeUser(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
        logger.info("++================== activeUser SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null || fkUserId == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().activeUser(token, fkUserId, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== activeUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== activeUser SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 15
     *
     * @param token      user token
     * @param fkUserId   id of user to be deActivated
     * @param fkSystemId id of system of changing user
     * @return simple StandardResponse to handle state
     */
    @Path("/deActive")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response deActiveUser(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
        logger.info("++================== deActiveUser SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null || fkUserId == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().deActiveUser(token, fkUserId, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== deActiveUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== deActiveUser SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 16
     * filter users
     *
     * @param encryptedRequest RequestFilterUsers
     * @return list of
     */
    @Path("/filter")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterUsers(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== filterUsers SERVICE : START ==================++");
        try {
            RequestFilterUsers request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestFilterUsers.class);
            StandardResponse<ResponseGetUsers> response = new UserImpl().filterUsers(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== filterUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== filterUsers SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }

    /**
     * 17
     * reset password of given user
     *
     * @param encryptedRequest RequestResetPassword
     * @return simple StandardResponse to handle state
     */
    @Path("/resetPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== resetPassword SERVICE : START ==================++");
        try {
            RequestResetPassword request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestResetPassword.class);
            StandardResponse response = new UserImpl().resetPassword(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== resetPassword SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== resetPassword SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }

    /***
     * reset password of user for his boss
     * @param token
     * @param fkUserId
     * @return
     * @throws Exception
     */
    @Path("/resetPassword")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId) throws Exception {
        logger.info("++================== getRoles SERVICE : START ==================++");
        try {
            if (token == null || fkUserId == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().resetPassword(token, fkUserId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getRoles SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getRoles SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 18
     * {@link GET} service with {@link QueryParam token} to return all roles of current user
     *
     * @param token      token of currentUser
     * @param fkSystemId id of system
     * @return all roles of current user
     */
    @Path("/roles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRoles(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
        logger.info("++================== getRoles SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().getUserRoles(token, fkUserId, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getRoles SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getRoles SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /***]
     * 25
     * assign a role to a user
     *
     * @param encryptedRequest encrypted request
     * @return simple StandardResponse to handle state
     */
    @Path("/roles")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response assignRole(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== assignRoles SERVICE : START ==================++");
        try {
            RequestAssignRole request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestAssignRole.class);
            StandardResponse response = new UserImpl().assignRole(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== assignRoles SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== assignRoles SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }

    /**
     * 19
     *
     * @param token      user token
     * @param fkSystemId id of system
     * @return list of roles with privileges of a user
     */
    @Path("/roles/privileges")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRolesWithPrivileges(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
        logger.info("++================== getUserRolesWithPrivileges SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetRolesWithPrivileges> response = new UserImpl().getUserRolesWithPrivileges(token, fkUserId, fkSystemId);
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
     * 20
     * {@link GET} service with {@link QueryParam token} to return all privileges of current user in specific system
     *
     * @param token      token of current User
     * @param fkSystemId fk system id of current User
     * @return list of privileges of currentUser in specific system
     */
    @Path("/privileges")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivileges(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().getPrivileges(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getPrivileges SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /***
     * 21
     * @param token token of a valid user
     * @return returns list of systems of a specific user
     */
    @Path("/systems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSystems(@QueryParam("token") String token) throws Exception {
        logger.info("++================== getUserSystems SERVICE : START ==================++");
        try {
            if (token == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getUserSystems(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUserSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getUserSystems SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /***
     * 32
     * @param token token of a valid user
     * @return returns list of systems of a specific user that logs on
     */
    @Path("/systems/login")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserLoginSystems(@QueryParam("token") String token) throws Exception {
        logger.info("++================== getUserLoginSystems SERVICE : START ==================++");
        try {
            if (token == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getUserLoginSystems(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUserLoginSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getUserLoginSystems SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /***
     * 28
     * @param token token of a valid user
     * @return returns list of systems of a specific user with fkUserId
     */
    @Path("/systems/id")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSystems(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId) throws Exception {
        logger.info("++================== getUserSystems SERVICE : START ==================++");
        try {
            if (token == null || fkUserId == null) {
                throw new ServerException(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getUserSystems(token, fkUserId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUserSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== getUserSystems SERVICE : EXCEPTION ==================++", ex, token);
        }
    }

    /**
     * 26
     *
     * @param encryptedRequest RequestAddUser
     * @return simple StandardResponse to handle state
     */
    @Path("/systems/assign")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignUserToSystem(EncryptedRequest encryptedRequest) {
        logger.info("++================== assignSystemToUser SERVICE : START ==================++");
        try {
            RequestAssignSystemToUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestAssignSystemToUser.class);
            StandardResponse response = new UserImpl().assignUserToSystem(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== assignSystemToUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== assignSystemToUser SERVICE : EXCEPTION ==================++", ex, encryptedRequest.getToken());
        }
    }
}
