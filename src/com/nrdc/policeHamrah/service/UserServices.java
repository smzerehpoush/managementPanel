package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.SystemImpl;
import com.nrdc.policeHamrah.impl.UserImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.*;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystems;
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

    @Path("/activate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response activeUser(EncryptedRequest encryptedRequest) {
        logger.info("++================== activeUser SERVICE : START ==================++");
        try {
            RequestActiveUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestActiveUser.class);
            StandardResponse response = new UserImpl().activeUser(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
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

    @Path("/deActivate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deActiveUser(EncryptedRequest encryptedRequest) {
        logger.info("++================== deActiveUser SERVICE : START ==================++");
        try {
            RequestDeActiveUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestDeActiveUser.class);
            StandardResponse response = new UserImpl().deActiveUser(encryptedRequest.getToken(), request);
            String key = UserDao.getKey(encryptedRequest.getToken()).getKey();
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

    @PUT
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

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== getUsers SERVICE : START ==================++");
        try {
            StandardResponse response = new UserImpl().getUsers(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== getUsers SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getUsers SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    @Path("/filter")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterUsers(EncryptedRequest encryptedRequest) {
        logger.info("++================== filterUsers SERVICE : START ==================++");
        try {
            RequestFilterUsers request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestFilterUsers.class);
            StandardResponse response = new UserImpl().filterUsers(encryptedRequest.getToken(), request);
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

    @Path("/edit")
    @POST
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

    @Path("/resetPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(EncryptedRequest encryptedRequest) {
        logger.info("++================== editUser SERVICE : START ==================++");
        try {
            RequestResetPassword request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestResetPassword.class);
            StandardResponse response = new UserImpl().resetPassword(encryptedRequest.getToken(), request);
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
     * {@link GET} service with {@link QueryParam token} to return all roles of current user
     *
     * @param token token of currentUser
     * @return all roles of current user
     */
    @Path("/roles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(@QueryParam("token") String token) {
        logger.info("++================== getRoles SERVICE : START ==================++");
        try {
            StandardResponse response = new UserImpl().getRoles(token);
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
     * {@link GET} service with {@link QueryParam token} to return all privileges of current user in specific system
     *
     * @param token token of current User
     * @param fkSystemId fk system id of current User
     * @return all privileges of currentUser in specific system
     */
    @Path("/privileges")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivileges(@QueryParam("token") String token, @QueryParam("fkSystemID") Long fkSystemId) {
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
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

    @Path("/roles/privileges")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRolesWithPrivileges(@QueryParam("token") String token) {
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
            StandardResponse response = new UserImpl().getRolesWithPrivileges(token);
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
     * @param token token of a valid user
     * @return returns list of systems of a specific user
     */
    @Path("/systems")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserSystems(@QueryParam("token") String token) {
        logger.info("++================== getUserSystems SERVICE : START ==================++");
        try {
            StandardResponse<ResponseGetSystems> response = new SystemImpl().getUserSystems(token);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getUserSystems SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getUserSystems SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }


}