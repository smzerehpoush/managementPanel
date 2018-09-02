package com.nrdc.managementPanel.model.dao;

import com.nrdc.managementPanel.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "USER_SYSTEM", schema = Constants.SCHEMA)
public class SystemUserDAO extends BaseModel {
    private Long id;
    private Long fkUserId;
    private Long fkSystemId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_USER_SYSTEM")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FK_USER_ID")
    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Basic
    @Column(name = "FK_SYSTEM_ID")
    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkRoleId) {
        this.fkSystemId = fkRoleId;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SystemUserDAO{");
        sb.append("id=").append(id);
        sb.append(", fkUserId=").append(fkUserId);
        sb.append(", fkSystemId=").append(fkSystemId);
        sb.append('}');
        return sb.toString();
    }
}
