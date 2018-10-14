package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dto.SystemVersionDTO;

import javax.persistence.*;

@Entity
@Table(name = "PH_SYSTEM_VERSIONS", schema = Constants.SCHEMA)
public class SystemVersion extends SystemVersionDTO {
    public static final String tableName="PH_SYSTEM_VERSION";
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_SYSTEM_VERSIONS", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", unique = true, table = tableName)
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }

    @Override
    @Basic
    @Column(name = "VERSION_NAME", unique = true, table = tableName)
    public String getVersionName() {
        return super.getVersionName();
    }

    @Override
    @Basic
    @Column(name = "VERSION_CODE", unique = true, table = tableName)
    public Long getVersionCode() {
        return super.getVersionCode();
    }

    @Override
    @Basic
    @Column(name = "APK_PATH", unique = true, table = tableName)
    public String getApkPath() {
        return super.getApkPath();
    }
}
