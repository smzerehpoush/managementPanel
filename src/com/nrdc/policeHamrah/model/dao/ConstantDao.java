package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.impl.Database;
import com.nrdc.policeHamrah.model.dto.ConstantDto;

import javax.persistence.*;

@Entity
@Table(name = "PH_CONSTANTS", schema = Constants.SCHEMA)
public class ConstantDao extends ConstantDto {
    private static final String tableName = "PH_CONSTANTS";

    public static String getConstant(String key) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (String) entityManager.createQuery("SELECT c.value FROM ConstantDao c WHERE c.key = :k ")
                    .setParameter("k", key)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new Exception(Constants.CONSTANT + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_CONSTANTS", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "KEY_NAME", table = tableName)
    public String getKey() {
        return super.getKey();
    }

    @Override
    @Basic
    @Column(name = "VALUE", table = tableName)
    public String getValue() {
        return super.getValue();
    }
}
