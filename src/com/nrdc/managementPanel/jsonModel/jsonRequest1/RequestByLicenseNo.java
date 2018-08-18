package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestByLicenseNo {
    private String licNo;

    public String getLicNo() {
        return licNo;
    }

    public void setLicNo(String licNo) {
        this.licNo = licNo;
    }

    @Override
    public String toString() {
        return "RequestByLicenseNo{" +
                "licenseNo='" + licNo + '\'' +
                '}';
    }
}
