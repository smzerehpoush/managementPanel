package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYSTEM_VERSIONS", schema = Constants.SCHEMA)
public class SystemVersionDAO extends BaseModel {
    private Long id;
    private Long fkSystemId;
    private String versionName;
    private Long versionCode;
    private String apkPath;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_SYSTEM_VERSIONS")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FK_SYSTEM_ID", unique = true, table = "SYSTEM_VERSIONS")
    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    @Basic
    @Column(name = "VERSION_NAME", unique = true, table = "SYSTEM_VERSIONS")
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Basic
    @Column(name = "VERSION_CODE", unique = true, table = "SYSTEM_VERSIONS")
    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    @Basic
    @Column(name = "APK_PATH", unique = true, table = "SYSTEM_VERSIONS")
    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
