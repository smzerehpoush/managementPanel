package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.ProvinceDto;

import javax.persistence.*;

@Entity
@Table(name = "PH_PROVINCE", schema = Constants.SCHEMA)
public class ProvinceDao extends ProvinceDto {
    public static final String tableName = "PH_PROVINCE";

    @Override
    @Basic
    @Column(name = "NUMBER_OF_USERS", table = tableName)
    public Long getNumberOfUsers() {
        return super.getNumberOfUsers();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_PROVINCE", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "NAME", table = tableName)
    public String getName() {
        return super.getName();
    }
}
