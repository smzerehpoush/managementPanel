package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.impl.LoginImpl;
import com.nrdc.managementPanel.impl.UserImpl;
import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.*;
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
    public Response activeUser(EncryptedRequest encryptedRequest){
        logger.info("++================== activeUser SERVICE : START ==================++");
        try {
            RequestActiveUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestActiveUser.class);
            StandardResponse response = new UserImpl().activeUser(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deActiveUser(EncryptedRequest encryptedRequest){
        logger.info("++================== deActiveUser SERVICE : START ==================++");
        try {
            RequestDeActiveUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestDeActiveUser.class);
            StandardResponse response = new UserImpl().deActiveUser(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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
    public Response addUser(EncryptedRequest encryptedRequest){
        logger.info("++================== addUser SERVICE : START ==================++");
        try {
            RequestAddUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestAddUser.class);
            StandardResponse response = new UserImpl().addUser(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(EncryptedRequest encryptedRequest){
        logger.info("++================== getUsers SERVICE : START ==================++");
        try {
            RequestGetUsers request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestGetUsers.class);
            StandardResponse response = new UserImpl().getUsers(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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
    public Response filterUsers(EncryptedRequest encryptedRequest){
        logger.info("++================== filterUsers SERVICE : START ==================++");
        try {
            RequestFilterUsers request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestFilterUsers.class);
            StandardResponse response = new UserImpl().filterUsers(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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
    public Response editUser(EncryptedRequest encryptedRequest){
        logger.info("++================== editUser SERVICE : START ==================++");
        try {
            RequestEditUser request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestEditUser.class);
            StandardResponse response = new UserImpl().editUser(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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
    public Response resetPassword(EncryptedRequest encryptedRequest){
        logger.info("++================== editUser SERVICE : START ==================++");
        try {
            RequestResetPassword request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestResetPassword.class);
            StandardResponse response = new UserImpl().resetPassword(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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

    @Path("/roles")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(EncryptedRequest encryptedRequest){
        logger.info("++================== getRoles SERVICE : START ==================++");
        try {
            RequestGetUserRolesWithPrivileges request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestGetUserRolesWithPrivileges.class);
            StandardResponse response = new UserImpl().getRoles(encryptedRequest.getToken(), request);
            String key = Database.getUserKey(encryptedRequest.getToken()).getKey();
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
    @Path("/roles/privileges")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivileges(EncryptedRequest encryptedRequest){
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
            RequestGetUserRolesWithPrivileges request = objectMapper.readValue(Encryption.decryptRequest(encryptedRequest), RequestGetUserRolesWithPrivileges.class);
            StandardResponse response = new UserImpl().getRolesWithPrivileges(encryptedRequest.getToken(), request);
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
