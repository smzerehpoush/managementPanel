package com.nrdc.managementPanel.jsonModel.jsonResponse;

/**
 * Created by jvd.karimi on 12/19/2017.
 */
public class ResponseViolationByPlateNo {

    private String totalFine;

    public ResponseViolationByPlateNo() {
    }

    public ResponseViolationByPlateNo(String totalFine) {
        this.totalFine = totalFine;
    }

    public String getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(String totalFine) {
        this.totalFine = totalFine;
    }
}
