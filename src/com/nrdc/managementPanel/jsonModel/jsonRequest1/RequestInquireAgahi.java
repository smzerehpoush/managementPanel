package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestInquireAgahi {
    private String plateNo;

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    @Override
    public String toString() {
        return "RequestInquireAgahi{" +
                "plateNo='" + plateNo + '\'' +
                '}';
    }
}
