package com.nrdc.policeHamrah.model.dto;

import java.util.Objects;

public  abstract class SystemDto implements BaseModel {
    private Long id;
    private String systemName;
    private String systemPath;
    private String packageName;
    private String iconPath;
    private Long downloadCount;
    private String description;
    private String title;
    private String type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemPath() {
        return systemPath;
    }

    public void setSystemPath(String systemPath) {
        this.systemPath = systemPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SystemDto{");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemDto)) return false;
        SystemDto systemDto = (SystemDto) o;
        return Objects.equals(getSystemName(), systemDto.getSystemName()) &&
                Objects.equals(getSystemPath(), systemDto.getSystemPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSystemName(), getSystemPath());
    }
}
