package com.nrdc.policeHamrah.model.dto;

public abstract class SystemUserDto implements BaseModel {
    private Long id;
    private Long fkUserId;
    private Long fkSystemId;

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

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkRoleId) {
        this.fkSystemId = fkRoleId;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SystemUserDto{");
        sb.append("id=").append(id);
        sb.append(", fkUserId=").append(fkUserId);
        sb.append(", fkSystemId=").append(fkSystemId);
        sb.append('}');
        return sb.toString();
    }
}
