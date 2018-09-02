package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.impl.Database;

import javax.persistence.*;

@Entity
@Table(name = "PRIVILEGE", schema = Constants.SCHEMA)
public class PrivilegeDAO extends BaseModel{
    private Long id;
    private String privilegeText;

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
