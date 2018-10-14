package com.nrdc.managementPanel.service;

import com.nrdc.managementPanel.impl.LoginImpl;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestLogin;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginService {
    private static Logger logger = Logger.getLogger(LoginService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(RequestLogin requestLogin) {
        logger.info("++================== login SERVICE : START ==================++");
        try {
            StandardResponse response = new LoginImpl().login(requestLogin);
            Response finalResponse = Response.status(200).entity(response).build();
            logger.info("++================== login SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== login SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
            return finalResponse;
        } catch (Exception ex) {
            logger.error("++================== activeUser SERVICE : EXCEPTION ==================++");
            StandardResponse response = StandardResponse.getNOKExceptions(ex);
            return Response.status(200).entity(response).build();
        }
    }
}
