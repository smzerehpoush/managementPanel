package com.nrdc.policeHamrah.jsonModel.jsonResponse;

import com.nrdc.policeHamrah.jsonModel.customizedModel.SystemWithVersion;

import java.util.List;

public class ResponseGetSystemWithVersions {

    private List<SystemWithVersion> systemWithVersions;

    public List<SystemWithVersion> getSystemWithVersions() {
        return systemWithVersions;
    }

    public void setSystemWithVersions(List<SystemWithVersion> systemWithVersions) {
        this.systemWithVersions = systemWithVersions;
    }

    @Override
    public String toString() {
        return "ResponseGetSystemWithVersions{" +
                "systemWithVersions=" + systemWithVersions +
                '}';
    }
}
