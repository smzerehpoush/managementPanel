package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.LoginImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLogin;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestLoginToSystems;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//done
@Path("/login")
public class LoginService {
    private static Logger logger = Logger.getLogger(LoginService.class.getName());

    /***
     *
     * @param requestLogin {@link RequestLogin} for logging in PoliceHamrah system
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
     * setvice to login into different systems
     * @param request {@link RequestLoginToSystems}
     * @return StandardResponse with {@link com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseLogin}
     */
    @Path("/system")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginToSystem(EncryptedRequest request) {
        logger.info("++================== login-to-system SERVICE : START ==================++");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RequestLoginToSystems requestLogin = objectMapper.readValue(Encryption.decryptRequest(request), RequestLoginToSystems.class);
            StandardResponse response = new LoginImpl().loginToSystem(request.getToken(), requestLogin);
            String key = UserDao.getKey(request.getToken()).getKey();
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
