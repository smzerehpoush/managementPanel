package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.model.dto.KeyDTO;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PH_KEYS", schema = Constants.SCHEMA)
public class Key extends KeyDTO {
    public static final String tableName = "PH_KEYS";

    public Key() {
    }

    public Key(User user, System system) throws Exception {
        user.checkKey(system);
        this.setKey(UUID.randomUUID().toString());
        this.setFkSystemId(system.getId());
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
