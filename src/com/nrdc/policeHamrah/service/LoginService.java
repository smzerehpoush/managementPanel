package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.exceptions.ExceptionHandler;
import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.impl.LoginImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAuthenticateUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/login")
public class LoginService {
    private static Logger logger = Logger.getLogger(LoginService.class.getName());

    /***
     * 01
     * login to police hamrah system
     * @param requestLogin {@link RequestLogin}
     * @return standardResponse
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(RequestLogin requestLogin) throws Exception {
        logger.info("++================== login SERVICE : START ==================++");
        try {
            StandardResponse response = new LoginImpl().login(requestLogin);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== login SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== login SERVICE : EXCEPTION ==================++", ex);
        }
    }

    /**
     * 32
     *
     * @param encryptedRequest RequestAddUser
     * @return simple StandardResponse to handle state
     */
    @Path("/authenticateUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(EncryptedRequest encryptedRequest) throws Exception {
        logger.info("++================== authenticateUser SERVICE : START ==================++");
        try {
            RequestAuthenticateUser request = new ObjectMapper().readValue(Encryption.decryptRequest(encryptedRequest), RequestAuthenticateUser.class);
            StandardResponse response = new LoginImpl().authenticateUser(request);
            String key = Constants.DEFAULT_KEY;
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== authenticateUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== authenticateUser SERVICE : EXCEPTION ==================++", ex, Constants.DEFAULT_KEY);
        }
    }


    /**
     * 02
     * login to all other system except policeHamrah
     *
     * @param token      user token
     * @param fkSystemId id of system that user want to login
     * @return Response
     */
    @Path("/system")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginToSystem(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) throws Exception {
        logger.info("++================== login-to-system SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new ServerException(Constants.REQUEST + Constants.IS_NOT_VALID);
            }
            StandardResponse<ResponseLogin> response = new LoginImpl().loginToSystem(token, fkSystemId);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            if (systemDao.getSystemName().equals(SystemNames.VT_REPORT.name()) || systemDao.getSystemName().equals(SystemNames.VEHICLE_TICKET.name()) || systemDao.getSystemName().equals(SystemNames.AGAHI.name()))
                return Response.status(200).entity(response).build();

            EncryptedResponse encryptedResponse = Encryption.encryptResponse(response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== login-to-system SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            return ExceptionHandler.create("++================== login-to-system SERVICE : EXCEPTION ==================++", ex, token);
        }
    }
}
