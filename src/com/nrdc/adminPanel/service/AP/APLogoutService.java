package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.APLogoutImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestAdminLogout;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseAdminLogout;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/adminLogout")
public class APLogoutService extends SimpleService {
    private static Logger logger = Logger.getLogger(APLogoutService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(RequestAdminLogout requestAdminLogout) {
        logger.info("++================== admin login SERVICE : START ==================++");
        try {
            logger.info(requestAdminLogout);
            VTResponse<ResponseAdminLogout> response = new APLogoutImpl().logout(requestAdminLogout);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== admin login SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== admin login SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseAdminLogout> response = new VTResponse<ResponseAdminLogout>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
