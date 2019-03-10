package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.impl.Database;

import javax.persistence.*;

@Entity
@Table(name = "PH_PRIVILEGE", schema = Constants.SCHEMA)
public class PrivilegeDao extends com.nrdc.policeHamrah.model.dto.PrivilegeDto {
    private static final String tableName = "PH_PRIVILEGE";

    public PrivilegeDao() {
    }

    public PrivilegeDao(Long id, String privilegeText) {
        this.setId(id);
        this.setPrivilegeText(privilegeText);
    }

    public static PrivilegeDao getPrivilege(String privilege) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (PrivilegeDao) entityManager.createQuery("SELECT p FROM PrivilegeDao p WHERE p.privilegeText = :privilegeText ")
                    .setParameter("privilegeText", privilege)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new ServerException(Constants.NOT_VALID_PRIVILEGE);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }


    public static PrivilegeDao getPrivilege(PrivilegeNames privilegeName) throws Exception {
        return getPrivilege(privilegeName.name());
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

    @Basic
    @Column(name = "PRIVILEGE_LABEL", table = tableName)
    @Override
    public String getLabel() {
        return super.getLabel();
    }

    @Basic
    @Column(name = "VISIBLE", table = tableName)
    @Override
    public Boolean getVisible() {
        return super.getVisible();
    }
}
