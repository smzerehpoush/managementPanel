package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.ProvinceUserDto;

import javax.persistence.*;

@Entity
@Table(name = "PH_PROVINCE_USER", schema = Constants.SCHEMA)
public class ProvinceUserDao extends ProvinceUserDto {
    private static final String tableName = "PH_PROVINCE_USER";

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_PROVINCE_USER", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_USER_ID", table = tableName)
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Basic
    @Column(name = "FK_PROVINCE_ID", table = tableName)
    public Long getFkProvinceId() {
        return super.getFkProvinceId();
    }
}
