package com.nrdc.managementPanel.impl;


import com.nrdc.managementPanel.exceptions.NotValidTokenException;
import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.model.Key;
import com.nrdc.managementPanel.model.System;
import com.nrdc.managementPanel.model.Token;
import com.nrdc.managementPanel.model.User;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.List;

public class Database {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("managementPanelJPA");
    private static Logger logger = Logger.getLogger(Database.class.getName());

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void tokenValidation(String token) throws NotValidTokenException {
        EntityManager entityManager = Database.getEntityManager();

        try {
            logger.info("=======[ ADMIN TOKEN VALIDATION : START ]=======");
            logger.info("TOKEN : " + token);
            Query tokenValidationQuery = entityManager.createQuery("SELECT it FROM Token it WHERE it.token = :token")
                    .setParameter("token", token);
            List result = tokenValidationQuery.getResultList();
            if (result.size() == 0) {
                logger.info("token is not valid");
                throw new NotValidTokenException(Constants.NOT_VALID_TOKEN);
            } else {
                logger.info("token is valid");
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static Key getUserKey(String token, String systemName) throws Exception {
        Token.validateToken(token,systemName);
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (Key) entityManager.createQuery("SELECT k FROM Key k JOIN Token t ON k.fkUserId = t.fkUserId WHERE t.token = :token AND k.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName )")
                    .setParameter("systemName", systemName)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
    public static Key getUserKey(String token, SystemNames systemName) throws Exception {
        return getUserKey(token,systemName.name());
    }
    public static Key getUserKey(String token) throws Exception {
        return getUserKey(token,SystemNames.MANAGEMENT_PANEL.name());
    }

    public static User getUser(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static Key getUsernameKey(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (Key) entityManager.createQuery("SELECT k FROM Key k JOIN User u ON k.fkUserId = u.id WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static System getSystem(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (System) entityManager.createQuery("SELECT s FROM System s WHERE s.systemName =:systemPath")
                    .setParameter("systemPath", systemName)
                    .getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new Exception("System is not valid");
        }finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

    }
}