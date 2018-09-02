package com.nrdc.managementPanel.impl;


import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Database {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("managementPanelJPA");
    private static Logger logger = Logger.getLogger(Database.class.getName());

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }


}