package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.impl.Database;

import javax.persistence.*;

@Entity
@Table(name = "PRIVILEGE", schema = Constants.SCHEMA)
public class Privilege {
    private Long id;
    private String privilegeText;

    public Privilege() {
    }

    public Privilege(Long id, String privilegeText) {
        this.id = id;
        this.privilegeText = privilegeText;
    }
    public static Privilege getPrivilege(String privilege){
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (Privilege) entityManager.createQuery("SELECT p FROM Privilege p WHERE p.privilegeText = :privilegeText")
                    .setParameter("privilegeText", privilege)
                    .getSingleResult();
        }finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
    public static Privilege getPrivilege(PrivilegeNames privilegeName){
        return getPrivilege(privilegeName.name());
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PRIVILEGE")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PRIVILEGE_TEXT")
    public String getPrivilegeText() {
        return privilegeText;
    }

    public void setPrivilegeText(String privilege) {
        this.privilegeText = privilege;
    }

}
