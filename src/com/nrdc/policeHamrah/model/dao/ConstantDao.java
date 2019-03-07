package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.ConstantDto;

import javax.persistence.*;

@Entity
@Table(name = "PH_CONSTANTS", schema = Constants.SCHEMA)
public class ConstantDao extends ConstantDto {
    private static final String tableName = "PH_CONSTANTS";

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_CONSTANTS", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "NAME", table = tableName)
    public String getName() {
        return super.getName();
    }

    @Override
    @Basic
    @Column(name = "VALUE", table = tableName)
    public String getValue() {
        return super.getValue();
    }
}
