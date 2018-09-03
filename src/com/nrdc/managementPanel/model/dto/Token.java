package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.model.dao.TokenDAO;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "TOKEN", schema = Constants.SCHEMA)
public class Token extends TokenDAO {
    public Token() {
    }

    public Token(User user, System system) throws Exception {
        user.checkToken(system);
        this.setToken(UUID.randomUUID().toString());
        this.setFkSystemId(system.getId());
        this.setFkUserId(user.getId());
    }

    public static void validateToken(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            int size = entityManager.createQuery("SELECT t FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName)")
                    .setParameter("systemName", systemName)
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
        validateToken(token, systemName.name());
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_TOKEN")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_USER_ID")
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Basic
    @Column(name = "TOKEN")
    public String getToken() {
        return super.getToken();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID")
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }
}

