package com.nrdc.policeHamrah.jsonModel.jsonRequest;


public class RequestLoginToSystems {
    private Long fkSystemId;

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    @Override
    public String toString() {
        return "RequestLoginToSystems{" +
                "fkSystemId=" + fkSystemId +
                '}';
    }
}
