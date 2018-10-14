package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.model.dto.SystemDTO;
import org.apache.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "PH_SYSTEM", schema = Constants.SCHEMA)
public class System extends SystemDTO {
    public static final String tableName="PH_SYSTEM";
    private static Logger logger = Logger.getLogger(System.class.getName());

    public System() {

    }

    public static System getSystem(Long fkSystemId) throws Exception {
        EntityManager entityManager = com.nrdc.managementPanel.impl.Database.getEntityManager();
        try {
            System system = (System) entityManager.createQuery("SELECT s FROM System s WHERE s.id = :id")
                    .setParameter("id", fkSystemId)
                    .getSingleResult();
            logger.info(system);
            return system;

        } catch (NoResultException | NonUniqueResultException ex1) {
            throw new Exception(Constants.NOT_VALID_SYSTEM);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static System getSystem(String systemName) throws Exception {
        EntityManager entityManager = com.nrdc.managementPanel.impl.Database.getEntityManager();
        logger.info("Get System Info ");
        try {
            System system = (System) entityManager.createQuery("SELECT s FROM System s WHERE s.systemName = :systemName")
                    .setParameter("systemName", systemName)
                    .getSingleResult();
            logger.info(system);
            return system;

        } catch (NoResultException | NonUniqueResultException ex1) {
            throw new Exception(Constants.NOT_VALID_SYSTEM);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static System getSystem(SystemNames systemName) throws Exception {
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


}
