package com.nrdc.policeHamrah.model.dto;

public abstract  class RoleDto implements BaseModel {
    private Long id;
    private String role;
    private Long fkCreatorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String privilege) {
        this.role = privilege;
    }

    public Long getFkCreatorId() {
        return fkCreatorId;
    }

    public void setFkCreatorId(Long fkCreatorId) {
        this.fkCreatorId = fkCreatorId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoleDao{");
        sb.append("id=").append(id);
        sb.append(", role='").append(role).append('\'');
        sb.append(", fkCreatorId=").append(fkCreatorId);
        sb.append('}');
        return sb.toString();
    }
}
