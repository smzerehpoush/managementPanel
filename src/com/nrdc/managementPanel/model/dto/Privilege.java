package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.model.dao.PrivilegeDAO;

import javax.persistence.EntityManager;

public class Privilege extends PrivilegeDAO {
    public Privilege() {
    }

    public Privilege(Long id, String privilegeText) {
        this.setId(id);
        this.setPrivilegeText(privilegeText);
    }

    public static Privilege getPrivilege(String privilege) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (Privilege) entityManager.createQuery("SELECT p FROM Privilege p WHERE p.privilegeText = :privilegeText")
                    .setParameter("privilegeText", privilege)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new Exception(Constants.NOT_VALID_PRIVILEGE);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static Privilege getPrivilege(PrivilegeNames privilegeName) throws Exception {
        return getPrivilege(privilegeName.name());
    }


}
