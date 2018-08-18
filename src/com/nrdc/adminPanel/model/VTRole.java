package com.nrdc.adminPanel.model;

import javax.persistence.*;

@Entity
@Table(name = "VT_ROLE", schema = "MOBILE")
public class VTRole {
    private Long id;
    private String role;

    public VTRole() {
    }

    public VTRole(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_VT_ROLE")
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

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VTRole)) return false;

        VTRole role1 = (VTRole) o;

        return getRole().equals(role1.getRole());
    }

    @Override
    public int hashCode() {
        return getRole().hashCode();
    }
}
