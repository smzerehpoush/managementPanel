package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestReportSystem;
import com.nrdc.policeHamrah.model.dto.SystemReportDto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PH_SYSTEM_REPORT", schema = Constants.SCHEMA)
public class SystemReportDao extends SystemReportDto {
    public static final String tableName = "PH_SYSTEM_REPORT";

    public SystemReportDao() {
    }

    public SystemReportDao(RequestReportSystem requestReportSystem, Long fkUserId) {
        super(requestReportSystem, fkUserId);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_SYSTEM_REPORT", table = tableName)
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
    @Column(name = "REPORT_TEXT", table = tableName)
    public String getReportText() {
        return super.getReportText();
    }

    @Override
    @Basic
    @Column(name = "RATE", table = tableName)
    public Long getRate() {
        return super.getRate();
    }

    @Override
    @Basic
    @Column(name = "IMEI", table = tableName)
    public String getImei() {
        return super.getImei();
    }

    @Override
    @Basic
    @Column(name = "ANDROID_VERSION", table = tableName)
    public String getAndroidVersion() {
        return super.getAndroidVersion();
    }

    @Override
    @Basic
    @Column(name = "LATITUDE", table = tableName)
    public Double getLatitude() {
        return super.getLatitude();
    }

    @Override
    @Basic
    @Column(name = "LONGITUDE", table = tableName)
    public Double getLongitude() {
        return super.getLongitude();
    }

    @Override
    @Basic
    @Column(name = "MANUFACTURER_NAME", table = tableName)
    public String getManufacturerName() {
        return super.getManufacturerName();
    }

    @Override
    @Basic
    @Column(name = "MANUFACTURER_CODE", table = tableName)
    public String getManufacturerCode() {
        return super.getManufacturerCode();
    }

    @Override
    @Temporal(TemporalType.TIMESTAMP)
    @Basic
    @Column(name = "TIME", table = tableName)
    public Date getTime() {
        return super.getTime();
    }
}
