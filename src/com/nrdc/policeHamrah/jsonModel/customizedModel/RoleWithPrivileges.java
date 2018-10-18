package com.nrdc.policeHamrah.jsonModel.customizedModel;

import com.nrdc.policeHamrah.model.dto.PrivilegeDto;

import java.util.List;

public class RoleWithPrivileges {
    private Long id;
    private String role;
    private List<PrivilegeDto> privileges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<PrivilegeDto> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDto> privileges) {
        this.privileges = privileges;
    }
}
