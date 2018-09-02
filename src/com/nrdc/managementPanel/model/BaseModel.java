package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.impl.Database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;

public class BaseModel implements Serializable, Cloneable {
    public void persist() {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive())
                transaction.begin();
            entityManager.persist(this);
            if (transaction.isActive())
                transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
