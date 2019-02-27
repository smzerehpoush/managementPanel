package com.nrdc.policeHamrah.helper;

import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;

import javax.ws.rs.core.Response;

public class ExceptionHandler {
    public static Response buildResponse(Exception ex) {
        StandardResponse response = StandardResponse.getNOKExceptions(ex);
        EncryptedResponse encryptedResponse = Encryption.encryptResponse(response);
        return Response.status(Response.Status.OK).entity(encryptedResponse).build();
    }

    public static Response buildResponse(Exception ex, String key) {
        StandardResponse response = StandardResponse.getNOKExceptions(ex);
        EncryptedResponse encryptedResponse = Encryption.encryptResponse(key, response);
        return Response.status(Response.Status.OK).entity(encryptedResponse).build();
    }
}
