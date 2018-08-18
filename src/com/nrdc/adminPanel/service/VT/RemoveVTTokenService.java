package com.nrdc.adminPanel.service.VT;

import com.nrdc.adminPanel.impl.VT.RemoveVTTokenImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestRemoveToken;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseRemoveToken;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/removeVTToken")
public class RemoveVTTokenService extends SimpleService {
    private static Logger logger = Logger.getLogger(RemoveVTTokenService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(RequestRemoveToken requestRemoveToken) {
        logger.info("++================== removeVTToken SERVICE : START ==================++");
        try {
            logger.info(requestRemoveToken);
            VTResponse<ResponseRemoveToken> response = new RemoveVTTokenImpl().removeVTToken(requestRemoveToken);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== removeVTToken SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== removeVTToken SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseRemoveToken> response = new VTResponse<ResponseRemoveToken>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
