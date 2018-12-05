package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.SystemDownloadDto;

import javax.persistence.*;

@Entity
@Table(name = "PH_SYSTEM_DOWNLOAD", schema = Constants.SCHEMA)
public class SystemDownloadDao extends SystemDownloadDto {
    public static final String tableName = "PH_SYSTEM_DOWNLOAD";

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_SYSTEM_DOWNLOAD", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", table = tableName)
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }

    @Override
    @Basic
    @Column(name = "FK_USER_ID", table = tableName)
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Basic
    @Column(name = "VERSION_CODE", table = tableName)
    public Long getVersionCode() {
        return super.getVersionCode();
    }
}
