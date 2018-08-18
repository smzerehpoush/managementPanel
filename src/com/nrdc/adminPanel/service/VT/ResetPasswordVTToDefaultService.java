package com.nrdc.adminPanel.service.VT;

import com.nrdc.adminPanel.impl.VT.ResetPasswordVTToDefaultImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestResetPasswordDefault;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseResetPasswordDefault;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/resetPasswordVTToDefault")
public class ResetPasswordVTToDefaultService extends SimpleService {
    private static Logger logger = Logger.getLogger(ResetPasswordVTToDefaultService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(RequestResetPasswordDefault requestResetPasswordDefault) {
        logger.info("++================== resetPasswordDefault SERVICE : START ==================++");
        try {
            logger.info(requestResetPasswordDefault);
            VTResponse<ResponseResetPasswordDefault> response = new ResetPasswordVTToDefaultImpl().resetPassword(requestResetPasswordDefault);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== resetPasswordDefault SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== resetPasswordDefault SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseResetPasswordDefault> response = new VTResponse<ResponseResetPasswordDefault>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
