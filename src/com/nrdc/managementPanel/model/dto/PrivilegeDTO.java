package com.nrdc.managementPanel.model.dto;

public class PrivilegeDTO extends BaseModel {
    private Long id;
    private String privilegeText;

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
        final StringBuffer sb = new StringBuffer("PrivilegeDTO{");
        sb.append("id=").append(id);
        sb.append(", privilegeText='").append(privilegeText).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
