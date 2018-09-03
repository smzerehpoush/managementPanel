package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.model.dao.SystemDAO;
import org.apache.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM", schema = Constants.SCHEMA)
public class System extends SystemDAO {
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
    @Column(name = "ID_SYSTEM")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "SYSTEM_NAME", unique = true, table = "SYSTEM")
    public String getSystemName() {
        return super.getSystemName();
    }

    @Override
    @Basic
    @Column(name = "SYSTEM_PATH", unique = true, table = "SYSTEM")
    public String getSystemPath() {
        return super.getSystemPath();
    }

    @Override
    @Basic
    @Column(name = "PACKAGE_NAME", unique = true, table = "SYSTEM")
    public String getPackageName() {
        return super.getPackageName();
    }

    @Override
    @Basic
    @Column(name = "ICON_PATH", unique = true, table = "SYSTEM")
    public String getIconPath() {
        return super.getIconPath();
    }

    @Override
    @Basic
    @Column(name = "DOWNLOAD_COUNT", unique = true, table = "SYSTEM")
    public Long getDownloadCount() {
        return super.getDownloadCount();
    }

    @Override
    @Basic
    @Column(name = "DESCRIPTION", unique = true, table = "SYSTEM")
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @Basic
    @Column(name = "TITLE", unique = true, table = "SYSTEM")
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    @Basic
    @Column(name = "TYPE", unique = true, table = "SYSTEM")
    public String getType() {
        return super.getType();
    }


}
