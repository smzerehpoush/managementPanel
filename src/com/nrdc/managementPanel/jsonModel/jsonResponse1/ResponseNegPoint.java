package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseNegPoint {

    // TODO: 3/11/2018 change field name to negativePoint
    private String negPoint;
    // TODO: 3/11/2018 what is rulId - change it to correct format
    private String rulId;

    public String getNegPoint() {
        return negPoint;
    }

    public void setNegPoint(String negPoint) {
        this.negPoint = negPoint;
    }

    public String getRulId() {
        return rulId;
    }

    public void setRulId(String rulId) {
        this.rulId = rulId;
    }
}
