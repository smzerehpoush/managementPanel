package com.nrdc.adminPanel.impl.VT;

import com.nrdc.adminPanel.helper.Constants;
import com.nrdc.adminPanel.impl.DBManager;
import com.nrdc.adminPanel.impl.Database;
import com.nrdc.adminPanel.jsonModel.VTResponse;
import com.nrdc.adminPanel.jsonModel.jsonRequest.RequestReportTicketsByTime;
import com.nrdc.adminPanel.jsonModel.jsonResponse.ResponseReportTickets;
import com.nrdc.adminPanel.model.CustomTicket;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ReportTicketsByTimeImpl {
    private EntityManager entityManager = DBManager.getEntityManager();

    public VTResponse<ResponseReportTickets> getTickets(RequestReportTicketsByTime requestReportTicketsByTime) throws Exception {
        String token = requestReportTicketsByTime.getToken();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Database.adminTokenValidation(token);
            stringBuilder.append(" SELECT ticket.id,ticket.enfDate,ticket.status FROM VTTicket ticket WHERE  1=1 ");
            if (requestReportTicketsByTime.getFkUserIdList().size() != 0) {
                stringBuilder.append("AND ( 1=0 ");
                for (Long fkUserId : requestReportTicketsByTime.getFkUserIdList()) {
                    stringBuilder.append(" OR ticket.fkUserId = ");
                    stringBuilder.append(fkUserId);
                }
                stringBuilder.append(" ) ");
            }
            stringBuilder.append(" AND ticket.enfDate >= :start AND ticket.enfDate <= :end ");
            stringBuilder.append(" ORDER BY ticket.enfDate DESC ");
            entityManager.getEntityManagerFactory().getCache().evictAll();
            Query query = entityManager.createQuery(stringBuilder.toString());
            query.setParameter("start", new Date(requestReportTicketsByTime.getStart()), TemporalType.TIMESTAMP);
            query.setParameter("end", new Date(requestReportTicketsByTime.getEnd()), TemporalType.TIMESTAMP);
            List<Object[]> resultList = query.getResultList();
            List<CustomTicket> tickets = new LinkedList<>();
            CustomTicket ticket;
            for (Object[] t : resultList) {
                ticket = new CustomTicket();


                ticket.setId((Long) t[0]);
                ticket.setDate((Date) t[1]);
                ticket.setStatus((String) t[2]);
                tickets.add(ticket);
            }
            ResponseReportTickets responseReportTickets = new ResponseReportTickets();
            responseReportTickets.setTickets(tickets);
            VTResponse<ResponseReportTickets> vtResponse = new VTResponse<>();
            vtResponse.setResponse(responseReportTickets);
            vtResponse.setResultCode(1);
            vtResponse.setResultMessage("OK");
            return vtResponse;
        } catch (Exception e) {
            throw new Exception(Constants.UNKNOWN_ERROR);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
}
