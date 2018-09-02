package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "TOKEN", schema = Constants.SCHEMA)
public class TokenDAO extends BaseModel {
    private Long id;
    private Long fkUserId;
    private String token;
    private Long fkSystemId;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_TOKEN")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FK_USER_ID")
    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Basic
    @Column(name = "TOKEN")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "FK_SYSTEM_ID")
    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TokenDAO{");
        sb.append("id=").append(id);
        sb.append(", fkUserId=").append(fkUserId);
        sb.append(", token='").append(token).append('\'');
        sb.append(", fkSystemId=").append(fkSystemId);
        sb.append('}');
        return sb.toString();
    }
}

