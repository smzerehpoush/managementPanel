package com.nrdc.policeHamrah.jsonModel.jsonRequest;

public class RequestLogout {
    private Long fkSystemId;


    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestLogout{");
        sb.append("fkSystemId=").append(fkSystemId);
        sb.append('}');
        return sb.toString();
    }
}
