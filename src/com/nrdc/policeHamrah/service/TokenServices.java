package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.impl.TokenImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/token")
public class TokenServices {
    private static Logger logger = Logger.getLogger(TokenServices.class.getName());

    /**
     * @param token      user token
     * @param fkSystemId id of system
     * @param fkUserId   id of user
     * @return simple StandardResponse to handle state
     */
    @Path("/remove")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeToken(@QueryParam("token") String token, @QueryParam("fkSystemId") Long fkSystemId, @QueryParam("fkUserId") Long fkUserId) {
        logger.info("++================== removeToken SERVICE : START ==================++");
        try {
            StandardResponse response = new TokenImpl().removeToken(token, fkSystemId, fkUserId);
            String key = UserDao.getKey(token).getKey();
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
            Response finalResponse = Response.status(200).entity(encryptedResponse).build();
            logger.info("++================== removeToken SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== removeToken SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
}
