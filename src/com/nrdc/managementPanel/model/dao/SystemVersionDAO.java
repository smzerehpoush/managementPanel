package com.nrdc.managementPanel.model.dao;

public class SystemVersionDAO extends BaseModel {
    private Long id;
    private Long fkSystemId;
    private String versionName;
    private Long versionCode;
    private String apkPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SystemVersionDAO{");
        sb.append("id=").append(id);
        sb.append(", fkSystemId=").append(fkSystemId);
        sb.append(", versionName='").append(versionName).append('\'');
        sb.append(", versionCode=").append(versionCode);
        sb.append(", apkPath='").append(apkPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
