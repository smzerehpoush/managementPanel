package com.nrdc.policeHamrah.service;

import com.nrdc.policeHamrah.helper.ABISImpl;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/abis")
public class ABISService {
    private static Logger logger = Logger.getLogger(ABISService.class.getName());

    @Path("/{serviceName}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ABISService(Object request, @PathParam("serviceName") String serviceName) {
        logger.info("++================== ABIS SERVICE : START ==================++");
        try {
            String response = new ABISImpl().sendRequest(request, serviceName);
            Response finalResponse = Response.status(Response.Status.OK).entity(response).build();
            logger.info("++================== ABIS SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            String response = "2000";
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }

    @Path("/{serviceName}/{pathParam}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ABISService(@PathParam("serviceName") String serviceName, @PathParam("pathParam") String pathParam) {
        logger.info("++================== ABIS SERVICE : START ==================++");
        try {
            String response = new ABISImpl().sendRequest(serviceName, pathParam);
            Response finalResponse = Response.status(Response.Status.OK).entity(response).build();
            logger.info("++================== ABIS SERVICE : END ==================++");
            return finalResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            String response = "2000";
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
}
