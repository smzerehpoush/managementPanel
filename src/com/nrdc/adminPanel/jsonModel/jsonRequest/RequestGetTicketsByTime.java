package com.nrdc.adminPanel.jsonModel.jsonRequest;

public class RequestGetTicketsByTime {
    private String token;
    private Long start;
    private Long end;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "RequestGetTicketsByTime{" +
                "token='" + token + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
