package com.nrdc.policeHamrah.jsonModel.jsonResponse;

import com.nrdc.policeHamrah.model.dto.SystemDto;

import java.util.List;

public class ResponseGetSystems {
    private List<SystemDto> systemDtos;

    public List<SystemDto> getSystemDtos() {
        return systemDtos;
    }

    public void setSystemDtos(List<SystemDto> systemDtos) {
        this.systemDtos = systemDtos;
    }
}
