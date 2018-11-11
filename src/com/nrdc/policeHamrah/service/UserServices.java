package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.SystemImpl;
import com.nrdc.policeHamrah.impl.UserImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAddUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestEditUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestFilterUsers;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestResetPassword;
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
     * @param encryptedRequest RequestAddUser
     * @return simple StandardResponse to handle state
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(EncryptedRequest encryptedRequest) {
        logger.info("++================== addUser SERVICE : START ==================++");
        try {
            RequestAddUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestAddUser.class);
            StandardResponse response = new UserImpl().addUser(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== addUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== addUser SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * 13
     * @param encryptedRequest RequestEditUser
     * @return simple StandardResponse to handle state
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(EncryptedRequest encryptedRequest) {
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
            logger.error("++================== editUser SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * 14
     * @param token      user token
     * @param fkUserId   id of user to be activated
     * @param fkSystemId id of system of changing user
     * @return simple StandardResponse to handle state
     */
    @Path("/active")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response activeUser(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== activeUser SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null || fkUserId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().activeUser(token, fkUserId, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== activeUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== activeUser SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * 15
     * @param token      user token
     * @param fkUserId   id of user to be deActivated
     * @param fkSystemId id of system of changing user
     * @return simple StandardResponse to handle state
     */
    @Path("/deActive")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response deActiveUser(@QueryParam("token") String token, @QueryParam("fkUserId") Long fkUserId, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== deActiveUser SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null || fkUserId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().deActiveUser(token, fkUserId, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== deActiveUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== deActiveUser SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
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
    public Response filterUsers(EncryptedRequest encryptedRequest) {
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
            logger.error("++================== filterUsers SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
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
    public Response resetPassword(EncryptedRequest encryptedRequest) {
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
            logger.error("++================== resetPassword SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }


    /**
     * 18
     * {@link GET} service with {@link QueryParam token} to return all roles of current user
     *
     * @param token token of currentUser
     * @param fkSystemId id of system
     * @return all roles of current user
     */
    @Path("/roles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRoles(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getRoles SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().getUserRoles(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
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

    /**
     * 19
     * @param token      user token
     * @param fkSystemId id of system
     * @return list of roles with privileges of a user
     */
    @Path("/roles/privileges")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRolesWithPrivileges(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getUserRolesWithPrivileges SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetRolesWithPrivileges> response = new UserImpl().getUserRolesWithPrivileges(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUserRolesWithPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getUserRolesWithPrivileges SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
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
    public Response getPrivileges(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse response = new UserImpl().getPrivileges(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
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

    /***
     * 21
     * @param token token of a valid user
     * @return returns list of systems of a specific user
     */
    @Path("/systems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSystems(@QueryParam("token") String token) {
        logger.info("++================== getUserSystems SERVICE : START ==================++");
        try {
            if (token == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getUserSystems(token);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUserSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getUserSystems SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }


}
