package com.nrdc.policeHamrah.impl;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Database {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("policeHamrahJPA");
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }


}