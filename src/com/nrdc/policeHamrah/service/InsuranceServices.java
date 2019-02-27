package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.helper.ExceptionHandler;
import com.nrdc.policeHamrah.impl.InsuranceImpl;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InsuranceServices {
    @GET
    @Path("/insurance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insurance( @QueryParam("plateNo") String plateNo) {
        try {
            StandardResponse response = new InsuranceImpl().getInfo(plateNo);
            EncryptedResponse encryptedResponse = Encryption.encryptResponse(response);
            return Response.status(Response.Status.OK).entity(encryptedResponse).build();
        } catch (Exception e) {
            return ExceptionHandler.buildResponse(e);
        }
    }
}
