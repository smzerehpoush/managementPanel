package com.nrdc.adminPanel.jsonModel.jsonResponse;

import com.nrdc.adminPanel.model.CustomTicket;

import java.util.List;

public class ResponseReportTickets {
    private List<CustomTicket> tickets;

    public List<CustomTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<CustomTicket> tickets) {
        this.tickets = tickets;
    }
}
