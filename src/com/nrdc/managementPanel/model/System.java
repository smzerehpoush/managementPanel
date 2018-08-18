package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYSTEM", schema = Constants.SCHEMA)
public class System implements Serializable {
    private static Logger logger = Logger.getLogger(System.class.getName());
    private Long id;
    private String systemName;
    private String systemPath;
    private String packageName;
    private String iconPath;
    private Long downloadCount;
    private String description;
    private String title;
    private String type;

    public System() {

    }

    public System(System system) {
        this.id = system.id;
        this.systemName = system.systemName;
        this.systemPath = system.systemPath;
        this.packageName = system.packageName;
        this.iconPath = system.iconPath;
        this.downloadCount = system.downloadCount;
        this.description = system.description;
        this.title = system.title;
        this.type = system.type;
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

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_SYSTEM")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SYSTEM_NAME", unique = true, table = "SYSTEM")
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @Basic
    @Column(name = "SYSTEM_PATH", unique = true, table = "SYSTEM")
    public String getSystemPath() {
        return systemPath;
    }

    public void setSystemPath(String systemPath) {
        this.systemPath = systemPath;
    }

    @Basic
    @Column(name = "PACKAGE_NAME", unique = true, table = "SYSTEM")
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Basic
    @Column(name = "ICON_PATH", unique = true, table = "SYSTEM")
    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Basic
    @Column(name = "DOWNLOAD_COUNT", unique = true, table = "SYSTEM")
    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Basic
    @Column(name = "DESCRIPTION", unique = true, table = "SYSTEM")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "TITLE", unique = true, table = "SYSTEM")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "TYPE", unique = true, table = "SYSTEM")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "System{" +
                "id=" + id +
                ", systemName='" + systemName + '\'' +
                ", systemPath='" + systemPath + '\'' +
                ", packageName='" + packageName + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", downloadCount=" + downloadCount +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
