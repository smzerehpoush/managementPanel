package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.LoginImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseLogin;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginService {
    private static Logger logger = Logger.getLogger(LoginService.class.getName());

    /***
     * 01
     * @param requestLogin {@link RequestLogin}
     * @return standardResponse
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(RequestLogin requestLogin) {
        logger.info("++================== login SERVICE : START ==================++");
        try {
            StandardResponse response = new LoginImpl().login(requestLogin);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== login SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== login SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

    /**
     * 02
     *
     * @param token      user token
     * @param fkSystemId id of system that user want to login
     * @return Response
     */
    @Path("/system")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginToSystem(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId) {
        logger.info("++================== login-to-system SERVICE : START ==================++");
        try {
            if (token == null || fkSystemId == null) {
                throw new Exception(Constants.NOT_VALID_REQUEST);
            }
            StandardResponse<ResponseLogin> response = new LoginImpl().loginToSystem(token, fkSystemId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== login-to-system SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== login-to-system SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }

}
