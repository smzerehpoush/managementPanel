package com.nrdc.policeHamrah.jsonModel.jsonResponse;

import com.nrdc.policeHamrah.model.dto.PrivilegeDto;

import java.util.List;

public class ResponseGetPrivileges {
    private List<PrivilegeDto> privileges;

    public List<PrivilegeDto> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDto> privileges) {
        this.privileges = privileges;
    }
}
