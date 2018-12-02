package com.nrdc.policeHamrah.exceptions;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;

public class ServerException extends Exception {
    private static Logger logger = Logger.getLogger(ServerException.class.getName());

    public static Response create(String message, Exception ex, String token) throws Exception {
        logger.error(message);
        logger.error(ex.getMessage(), ex);
        StandardResponse response = StandardResponse.getNOKExceptions(ex);
        String key = UserDao.getKey(token).getKey();
        EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
        return Response.status(200).entity(encryptedResponse).build();
    }
}
