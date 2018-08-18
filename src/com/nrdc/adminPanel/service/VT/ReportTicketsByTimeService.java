package com.nrdc.adminPanel.service.VT;

import com.nrdc.adminPanel.impl.VT.ReportTicketsByTimeImpl;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestReportTicketsByTime;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseLastTickets;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseReportTickets;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("reportTicketsByTime")
public class ReportTicketsByTimeService   {
    private static Logger logger = Logger.getLogger(ReportTicketsByTimeService.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDriverInfoFromLic(RequestReportTicketsByTime requestReportTicketsByTime) throws IOException {
        try {
            logger.info(requestReportTicketsByTime);
            VTResponse<ResponseReportTickets> response = new ReportTicketsByTimeImpl().getTickets(requestReportTicketsByTime);
            return Response.status(200).entity(response).build();
        } catch (Exception ex) {
            VTResponse<ResponseLastTickets> response = new VTResponse<ResponseLastTickets>().getNOKExceptions(ex.getMessage());
            return Response.status(200).entity(response).build();
        }
    }
}
