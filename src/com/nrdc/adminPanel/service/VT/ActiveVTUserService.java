package com.nrdc.adminPanel.service.VT;

import com.nrdc.adminPanel.impl.VT.ActiveVTUserImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestActiveUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseActiveUser;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseDeActiveUser;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/activeVTUser")
public class ActiveVTUserService extends SimpleService {
    private static Logger logger = Logger.getLogger(ActiveVTUserService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response activeUser(RequestActiveUser requestActiveUser) {
        logger.info("++================== deActiveUser SERVICE : START ==================++");
        try {
            logger.info(requestActiveUser);
            VTResponse<ResponseActiveUser> response = new ActiveVTUserImpl().activeUser(requestActiveUser);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== deActiveUser SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== deActiveUser SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseDeActiveUser> response = new VTResponse<ResponseDeActiveUser>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
