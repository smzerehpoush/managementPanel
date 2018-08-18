package com.nrdc.adminPanel.jsonModel.jsonRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestTicketList {

    private String token;

    private List<RequestTicket> tickets = new ArrayList<>();


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<RequestTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<RequestTicket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "RequestTicketList{" +
                "token='" + token + '\'' +
                ", tickets=" + tickets +
                '}';
    }
}
