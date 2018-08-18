package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.AP.GetPrivilegesImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestGetPrivileges;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getPrivileges")
public class GetPrivilegesService extends SimpleService {
    private static Logger logger = Logger.getLogger(GetPrivilegesService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserPrivileges(RequestGetPrivileges requestGetPrivileges) {
        logger.info("++================== getPrivileges SERVICE : START ==================++");
        try {
            logger.info(requestGetPrivileges);
            VTResponse<ResponseGetPrivileges> response = new GetPrivilegesImpl().getPrivileges(requestGetPrivileges);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== getPrivileges SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== getPrivileges SERVICE : EXCEPTION ==================++");
            VTResponse<ResponseGetPrivileges> response = new VTResponse<ResponseGetPrivileges>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }


}
