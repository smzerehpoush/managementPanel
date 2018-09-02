package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "KEYS", schema = Constants.SCHEMA)
public class KeyDAO extends BaseModel {

    private Long id;
    private String key;
    private Long fkSystemId;
    private Long fkUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_KEYS")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "KEY", table = "KEYS")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Basic
    @Column(name = "FK_SYSTEM_ID", table = "KEYS")
    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    @Basic
    @Column(name = "FK_USER_ID", table = "KEYS")
    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Override
    public String toString() {
        return "Key{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", fkSystemId=" + fkSystemId +
                ", fkUserId=" + fkUserId +
                '}';
    }

}
