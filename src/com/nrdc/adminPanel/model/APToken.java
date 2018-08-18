package com.nrdc.adminPanel.model;

import com.nrdc.adminPanel.impl.DBManager;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AP_TOKEN", schema = "MOBILE")
public class APToken {
    private Long id;
    private Long fkUserId;
    private String token;
    private Date startDate;

    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public APToken() {
    }

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_AP_TOKEN")
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

}
