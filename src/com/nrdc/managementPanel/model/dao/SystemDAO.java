package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM", schema = Constants.SCHEMA)
public class SystemDAO extends BaseModel {
    private Long id;
    private String systemName;
    private String systemPath;
    private String packageName;
    private String iconPath;
    private Long downloadCount;
    private String description;
    private String title;
    private String type;


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
        final StringBuffer sb = new StringBuffer("SystemDAO{");
        sb.append("id=").append(id);
        sb.append(", systemName='").append(systemName).append('\'');
        sb.append(", systemPath='").append(systemPath).append('\'');
        sb.append(", packageName='").append(packageName).append('\'');
        sb.append(", iconPath='").append(iconPath).append('\'');
        sb.append(", downloadCount=").append(downloadCount);
        sb.append(", description='").append(description).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
