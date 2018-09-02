package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.helper.Constants;

import javax.persistence.*;

@Entity
@Table(name = "ROLE", schema = Constants.SCHEMA)
public class Role {
    private Long id;
    private String role;
    private Long fkCreatorId;

    public Role() {
    }

    public Role(Long id, String privilege) {
        this.id = id;
        this.role = privilege;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ROLE")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ROLE")
    public String getRole() {
        return role;
    }

    public void setRole(String privilege) {
        this.role = privilege;
    }

    @Basic
    @Column(name = "FK_CREATOR_ID")
    public Long getFkCreatorId() {
        return fkCreatorId;
    }

    public void setFkCreatorId(Long fkCreatorId) {
        this.fkCreatorId = fkCreatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role privilege1 = (Role) o;

        if (!getId().equals(privilege1.getId())) return false;
        return getRole().equals(privilege1.getRole());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getRole().hashCode();
        return result;
    }
}
