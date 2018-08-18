package com.nrdc.adminPanel.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jvd.karimi on 2/27/2018.
 */
@Entity
@Table(name = "AP_VERSION", schema = "MOBILE")
public class APVersion {
    private int id;
    private Timestamp releaseDate;
    private Long version;
    private String apkPath;

    public APVersion() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_AP_VERSION")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "RELEASE_DATE")
    public Timestamp getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Timestamp releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Basic
    @Column(name = "VERSION")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long verseion) {
        this.version = verseion;
    }

    @Basic
    @Column(name = "APK_PATH")
    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APVersion that = (APVersion) o;

        if (id != that.id) return false;
        if (releaseDate != null ? !releaseDate.equals(that.releaseDate) : that.releaseDate != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        return apkPath != null ? apkPath.equals(that.apkPath) : that.apkPath == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (apkPath != null ? apkPath.hashCode() : 0);
        return result;
    }
}
