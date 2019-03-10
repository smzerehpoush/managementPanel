package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.impl.Database;
import com.nrdc.policeHamrah.model.dto.SystemDto;

import javax.persistence.*;

@Entity
@Table(name = "PH_SYSTEM", schema = Constants.SCHEMA)
public class SystemDao extends SystemDto {
    public static final String tableName = "PH_SYSTEM";

    public SystemDao() {

    }

    public static SystemDao getSystem(Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            SystemDao systemDao = (SystemDao) entityManager.createQuery("SELECT s FROM SystemDao s WHERE s.id = :id")
                    .setParameter("id", fkSystemId)
                    .getSingleResult();
            return systemDao;

        } catch (NoResultException | NonUniqueResultException ex1) {
            throw new ServerException(Constants.NOT_VALID_SYSTEM);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public static SystemDao getSystem(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            SystemDao systemDao = (SystemDao) entityManager.createQuery("SELECT s FROM SystemDao s WHERE s.systemName = :systemName")
                    .setParameter("systemName", systemName)
                    .getSingleResult();
            return systemDao;

        } catch (NoResultException | NonUniqueResultException ex1) {
            throw new ServerException(Constants.NOT_VALID_SYSTEM);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public static SystemDao getSystem(SystemNames systemName) throws Exception {
        return getSystem(systemName.name());
    }


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_SYSTEM", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "SYSTEM_NAME", unique = true, table = tableName)
    public String getSystemName() {
        return super.getSystemName();
    }

    @Override
    @Basic
    @Column(name = "SYSTEM_PATH", table = tableName)
    public String getSystemPath() {
        return super.getSystemPath();
    }

    @Override
    @Basic
    @Column(name = "PACKAGE_NAME", unique = true, table = tableName)
    public String getPackageName() {
        return super.getPackageName();
    }

    @Override
    @Basic
    @Column(name = "ICON_PATH", table = tableName)
    public String getIconPath() {
        return super.getIconPath();
    }

    @Override
    @Basic
    @Column(name = "DOWNLOAD_COUNT", unique = true, table = tableName)
    public Long getDownloadCount() {
        return super.getDownloadCount();
    }

    @Override
    @Basic
    @Column(name = "DESCRIPTION", unique = true, table = tableName)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @Basic
    @Column(name = "TITLE", unique = true, table = tableName)
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    @Basic
    @Column(name = "TYPE", unique = true, table = tableName)
    public String getType() {
        return super.getType();
    }

    @Override
    @Basic
    @Column(name = "RATE", unique = true, table = tableName)
    public Double getRate() {
        return super.getRate();
    }

    @Override
    @Basic
    @Column(name = "RATE_COUNT", unique = true, table = tableName)
    public Long getRateCount() {
        return super.getRateCount();
    }
}
