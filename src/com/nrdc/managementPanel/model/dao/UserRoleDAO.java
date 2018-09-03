package com.nrdc.managementPanel.model.dao;

public class UserRoleDAO extends BaseModel {
    private Long id;
    private Long fkUserId;
    private Long fkRoleId;

    public UserRoleDAO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Long getFkRoleId() {
        return fkRoleId;
    }

    public void setFkRoleId(Long fkRoleId) {
        this.fkRoleId = fkRoleId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserRoleDAO{");
        sb.append("id=").append(id);
        sb.append(", fkUserId=").append(fkUserId);
        sb.append(", fkRoleId=").append(fkRoleId);
        sb.append('}');
        return sb.toString();
    }
}
