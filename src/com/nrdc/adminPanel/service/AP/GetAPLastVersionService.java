package com.nrdc.adminPanel.service.AP;

import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseLastVersion;
import com.nrdc.adminPanel.model.APVersion;
import com.nrdc.adminPanel.service.SimpleService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getApLastVersion")

public class GetAPLastVersionService extends SimpleService {
    private static Logger logger = Logger.getLogger(GetAPLastVersionService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastVersion() {

        logger.info("++================== getLastVersion SERVICE : START ==================++");
        EntityManager entityManager = null;

        try {
            entityManager = DBManager.getEntityManager();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query getLastVersionQuery = entityManager.createQuery("SELECT av FROM APVersion av where av.version = (select max(av2.version) from APVersion av2)");
            APVersion version = (APVersion) getLastVersionQuery.getSingleResult();
            ResponseLastVersion responseLastVersion = new ResponseLastVersion();
            responseLastVersion.setApkPath(version.getApkPath());
            responseLastVersion.setVersion(version.getVersion());
            VTResponse<ResponseLastVersion> response = new VTResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseLastVersion);
            Response finalResponse = Response.status(200).entity(responseLastVersion).build();
            logger.info("++================== getLastVersion SERVICE : END ==================++");
            return finalResponse;

        } catch (Exception ex) {
            logger.error("++================== getLastVersion SERVICE : EXCEPTION ==================++");
            logger.error(ex.getMessage(), ex);
            VTResponse response = new VTResponse();
            response.setResultCode(-1);
            response.setResultMessage(ex.getMessage());
            //this service dose not use encryption
            return Response.status(200).entity(response).build();
        }


    }
}
