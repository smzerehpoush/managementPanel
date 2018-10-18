package com.nrdc.policeHamrah.model.dto;

public abstract class PrivilegeDto implements BaseModel {
    private Long id;
    private String privilegeText;
    private Long fkSystemId;

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivilegeText() {
        return privilegeText;
    }

    public void setPrivilegeText(String privilege) {
        this.privilegeText = privilege;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PrivilegeDto{");
        sb.append("id=").append(id);
        sb.append(", privilegeText='").append(privilegeText).append('\'');
        sb.append(", fkSystemId=").append(fkSystemId);
        sb.append('}');
        return sb.toString();
    }
}
