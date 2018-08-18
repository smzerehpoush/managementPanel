package com.nrdc.managementPanel.jsonModel.jsonResponse;

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
