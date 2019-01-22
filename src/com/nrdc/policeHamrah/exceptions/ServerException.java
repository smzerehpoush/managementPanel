package com.nrdc.policeHamrah.exceptions;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;

public class ServerException extends Exception {
    private static Logger logger = Logger.getLogger(ServerException.class.getName());

    public static Response create(String message, Exception ex, String token) {
        logger.error(message);
        logger.error(ex.getMessage());
        StandardResponse response = StandardResponse.getNOKExceptions(ex);
        String key;
        if (token.equals(Constants.DEFAULT_KEY))
            key = Constants.DEFAULT_KEY;
        else
            try {
                key = UserDao.getKey(token).getKey();
            } catch (Exception e) {
                key = "GOFYS";
            }
        EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
        return Response.status(200).entity(encryptedResponse).build();
    }

    public static Response create(String message, Exception ex) {
        return create(message,ex,Constants.DEFAULT_KEY);
    }
}
