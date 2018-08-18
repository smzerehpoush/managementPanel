package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.exceptions.NotValidTokenException;
import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.helper.Constants;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@Entity
@Table(name = "TOKEN", schema = Constants.SCHEMA)
public class Token implements Serializable {
    private Long id;
    private Long fkUserId;
    private String token;
    private Long fkSystemId;
    public Token() {
    }


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
    public Token (User user, Systems system) throws Exception {
        user.checkToken(system);
        this.token = new BigInteger(40,  new SecureRandom()).toString(32);
        this.fkSystemId=system.getId();
        this.fkUserId = user.getId();
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", fkUserId=" + fkUserId +
                ", token='" + token + '\'' +
                ", fkSystemId=" + fkSystemId +
                '}';
    }
    public static void validateToken(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            int size = entityManager.createQuery("SELECT t FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM Systems s WHERE s.systemName = :systemName)")
                    .setParameter("systemName",systemName)
                    .setParameter("token", token)
                    .getResultList()
                    .size();
            if (size != 1) {
                throw new Exception(Constants.NOT_VALID_TOKEN);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
    public static void validateToken(String token, SystemNames systemName) throws Exception {
        validateToken(token,systemName.name());
    }
}
