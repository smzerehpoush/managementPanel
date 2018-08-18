package com.nrdc.adminPanel.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("vehicleTicketJPA");

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

}