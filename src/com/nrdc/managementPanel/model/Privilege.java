package com.nrdc.managementPanel.model;

import javax.persistence.*;

@Entity
@Table(name = "PH_PRIVILEGE", schema = "MOBILE")
public class Privilege {
    private Long id;
    private String privilege;

    public Privilege() {
    }

    public Privilege(Long id, String privilege) {
        this.id = id;
        this.privilege = privilege;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_VT_PRIVILEGE")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PRIVILEGE")
    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Privilege)) return false;

        Privilege privilege1 = (Privilege) o;

        if (!getId().equals(privilege1.getId())) return false;
        return getPrivilege().equals(privilege1.getPrivilege());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getPrivilege().hashCode();
        return result;
    }
}
