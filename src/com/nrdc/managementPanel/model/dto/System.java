package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.model.dao.SystemDAO;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

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


}
