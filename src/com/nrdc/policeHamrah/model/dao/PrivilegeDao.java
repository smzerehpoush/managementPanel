package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.impl.Database;

import javax.persistence.*;

@Entity
@Table(name = "PH_PRIVILEGE", schema = Constants.SCHEMA)
public class PrivilegeDao extends com.nrdc.policeHamrah.model.dto.PrivilegeDto {
    public static final String tableName = "PH_PRIVILEGE";

    public PrivilegeDao() {
    }

    public PrivilegeDao(Long id, String privilegeText) {
        this.setId(id);
        this.setPrivilegeText(privilegeText);
    }

    public static PrivilegeDao getPrivilege(String privilege, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (PrivilegeDao) entityManager.createQuery("SELECT p FROM PrivilegeDao p JOIN PrivilegeSystemDao ps ON p.id = ps .fkPrivilegeId WHERE p.privilegeText = :privilegeText AND ps.fkSystemId = :fkSystemId ")
                    .setParameter("privilegeText", privilege)
                    .setParameter("fkSystemId", fkSystemId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new Exception(Constants.NOT_VALID_PRIVILEGE);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static PrivilegeDao getPrivilege(String privilege) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (PrivilegeDao) entityManager.createQuery("SELECT p FROM PrivilegeDao p WHERE p.privilegeText = :privilegeText")
                    .setParameter("privilegeText", privilege)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new Exception(Constants.NOT_VALID_PRIVILEGE);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static PrivilegeDao getPrivilege(PrivilegeNames privilegeName) throws Exception {
        return getPrivilege(privilegeName.name());
    }

    public static PrivilegeDao getPrivilege(PrivilegeNames privilegeName, Long fkSystemId) throws Exception {
        return getPrivilege(privilegeName.name(), fkSystemId);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_PRIVILEGE", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "PRIVILEGE_TEXT", table = tableName)
    public String getPrivilegeText() {
        return super.getPrivilegeText();
    }

    @Override
    @Basic
    @Column(name = "FK_SYSTEM_ID", table = tableName)
    public Long getFkSystemId() {
        return super.getFkSystemId();
    }
}
