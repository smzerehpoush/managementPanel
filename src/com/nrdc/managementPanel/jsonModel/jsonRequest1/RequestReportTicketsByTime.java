package com.nrdc.managementPanel.jsonModel.jsonRequest;

import java.util.List;

public class RequestReportTicketsByTime {
    private String token;
    private Long start;
    private Long end;
    private List<Long> fkUserIdList;

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

    public List<Long> getFkUserIdList() {
        return fkUserIdList;
    }

    public void setFkUserIdList(List<Long> fkUserIdList) {
        this.fkUserIdList = fkUserIdList;
    }
}
