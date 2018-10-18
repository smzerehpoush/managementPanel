package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.impl.Database;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PH_TOKEN", schema = Constants.SCHEMA)
public class TokenDao extends com.nrdc.policeHamrah.model.dto.TokenDto {
    public static final String tableName = "PH_TOKEN";

    public TokenDao() {
    }

    public TokenDao(UserDao user, SystemDao systemDao) throws Exception {
        user.checkToken(systemDao);
        this.setToken(UUID.randomUUID().toString());
        this.setFkSystemId(systemDao.getId());
        this.setFkUserId(user.getId());
    }

    public static void validateToken(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            Long size = (Long) entityManager.createQuery("SELECT count (t) FROM TokenDao t JOIN SystemDao s ON s.id = t.fkSystemId WHERE t.token = :token AND s.systemName = :systemName")
                    .setParameter("systemName", systemName)
                    .setParameter("token", token)
                    .getSingleResult();
            if (!size.equals(1L)) {
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
    @Column(name = "ID_PH_TOKEN", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "FK_USER_ID", table = tableName)
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Basic
    @Column(name = "TOKEN", table = tableName)
    public String getToken() {
        return super.getToken();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", table = tableName)
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }
}

