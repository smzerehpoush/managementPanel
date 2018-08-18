package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.APLoginImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestAdminLogin;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAdminLogin;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/adminLogin")
public class APLoginService   {
    private static Logger logger = Logger.getLogger(APLoginService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(RequestAdminLogin requestAdminLogin) {
        logger.info("++================== adminLogin SERVICE : START ==================++");
        try {
            logger.info(requestAdminLogin);
            VTResponse<ResponseAdminLogin> responseAdminLogin = new APLoginImpl().adminLogin(requestAdminLogin);
            Response finalResponse = Response.status(200).entity(responseAdminLogin).build();
            logger.info("++================== adminLogin SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== adminLogin SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseAdminLogin> response = new VTResponse<ResponseAdminLogin>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
