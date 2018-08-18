package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.RemoveAPTokenImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestRemoveToken;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseRemoveToken;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/removeAPToken")
public class RemoveAPTokenService   {
    private static Logger logger = Logger.getLogger(RemoveAPTokenService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(RequestRemoveToken requestRemoveToken) {
        logger.info("++================== removeAPToken SERVICE : START ==================++");
        try {
            logger.info(requestRemoveToken);
            VTResponse<ResponseRemoveToken> response = new RemoveAPTokenImpl().removeAPToken(requestRemoveToken);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== removeAPToken SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== removeAPToken SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseRemoveToken> response = new VTResponse<ResponseRemoveToken>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
