package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.KeyDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PH_KEYS", schema = Constants.SCHEMA)
public class KeyDao extends KeyDto {
    public static final String tableName = "PH_KEYS";

    public KeyDao() {
    }

    public KeyDao(UserDao user, SystemDao systemDao) throws Exception {
        user.checkKey(systemDao);
        this.setKey(UUID.randomUUID().toString());
        this.setFkSystemId(systemDao.getId());
        this.setFkUserId(user.getId());
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_KEYS", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "KEY", table = tableName)
    public String getKey() {
        return super.getKey();
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
}
