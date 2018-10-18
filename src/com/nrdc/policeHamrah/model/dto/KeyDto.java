package com.nrdc.policeHamrah.model.dto;

public abstract class KeyDto extends BaseModel{

    private Long id;
    private String key;
    private Long fkSystemId;
    private Long fkUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getFkSystemId() {
        return fkSystemId;
    }

    public void setFkSystemId(Long fkSystemId) {
        this.fkSystemId = fkSystemId;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    @Override
    public String toString() {
        return "KeyDao{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", fkSystemId=" + fkSystemId +
                ", fkUserId=" + fkUserId +
                '}';
    }

}
