package com.nrdc.managementPanel.jsonModel.jsonResponse;

import com.nrdc.managementPanel.model.VTTicket;

import java.util.List;

public class ResponseLastTickets {
    private List<VTTicket> tickets;

    public List<VTTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<VTTicket> tickets) {
        this.tickets = tickets;
    }
}
