package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dto.SystemVersionDTO;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM_VERSIONS", schema = Constants.SCHEMA)
public class SystemVersion extends SystemVersionDTO {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_SYSTEM_VERSIONS")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", unique = true, table = "SYSTEM_VERSIONS")
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }

    @Override
    @Basic
    @Column(name = "VERSION_NAME", unique = true, table = "SYSTEM_VERSIONS")
    public String getVersionName() {
        return super.getVersionName();
    }

    @Override
    @Basic
    @Column(name = "VERSION_CODE", unique = true, table = "SYSTEM_VERSIONS")
    public Long getVersionCode() {
        return super.getVersionCode();
    }

    @Override
    @Basic
    @Column(name = "APK_PATH", unique = true, table = "SYSTEM_VERSIONS")
    public String getApkPath() {
        return super.getApkPath();
    }
}
