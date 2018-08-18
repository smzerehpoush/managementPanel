package com.nrdc.adminPanel.jsonModel.jsonResponse;

public class ResponseLastVersion {
    private Long version;
    private String apkPath;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
