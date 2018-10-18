package com.nrdc.policeHamrah.jsonModel.customizedModel;

import com.nrdc.policeHamrah.model.dto.SystemDto;
import com.nrdc.policeHamrah.model.dto.SystemVersionDto;

public class SystemWithVersion {
    private SystemVersionDto systemVersionDto;
    private SystemDto systemDto;

    public SystemVersionDto getSystemVersionDto() {
        return systemVersionDto;
    }


    public void setSystemVersionDto(SystemVersionDto systemVersionDto) {
        this.systemVersionDto = systemVersionDto;
    }

    public SystemDto getSystemDto() {
        return systemDto;
    }

    public void setSystemDto(SystemDto systemDto) {
        this.systemDto = systemDto;
    }

    @Override
    public String toString() {
        return "SystemWithVersion{" +
                "systemVersionDto=" + systemVersionDto +
                ", systemDto=" + systemDto +
                '}';
    }
}
