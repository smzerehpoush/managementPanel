package com.nrdc.adminPanel.model;

import com.nrdc.adminPanel.impl.DBManager;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "VT_TOKEN", schema = "MOBILE")
public class VTToken {
    private Long id;
    private Long fkUserId;
    private String token;
    private Date startDate;

    private EntityManager entityManager = DBManager.getEntityManager();
    private EntityTransaction transaction = entityManager.getTransaction();

    public VTToken() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_VT_TOKEN")
    public Long getId() {
        return id;
    }

    public void setId(Long pkTokenId) {
        this.id = pkTokenId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VTToken)) return false;

        VTToken vtToken = (VTToken) o;

        if (!getFkUserId().equals(vtToken.getFkUserId())) return false;
        return getToken().equals(vtToken.getToken());
    }

    @Override
    public int hashCode() {
        int result = getFkUserId().hashCode();
        result = 31 * result + getToken().hashCode();
        return result;
    }


}
