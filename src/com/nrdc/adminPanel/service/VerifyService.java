package com.nrdc.adminPanel.service;

import com.nrdc.adminPanel.impl.DBManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;

@Path("/verify")
public class VerifyService extends SimpleService {
    private static Logger logger = Logger.getLogger(VerifyService.class.getName());
    private EntityManager entityManager = DBManager.getEntityManager();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response verifyRESTService() {
        logger.info("++================== VERIFY SERVICE : START ==================++");

        String result =
                        "+-------------------------------+\n" +
                        "+           adminPanel          +\n" +
                        "+-------------------------------+\n" +
                        "+ Services is run               +\n" +
                        "+ Version 1.0                   +\n" +
                        "+ Time : " + new Date().toString() + "\n" +
                        "+-------------------------------+";
        Response finalResponse = Response.status(200).entity(result).build();
        logger.info("++================== VERIFY SERVICE : END ==================++");
        return finalResponse;
    }

}
