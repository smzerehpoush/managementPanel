package com.nrdc.managementPanel.jsonModel.jsonResponse;

import com.nrdc.managementPanel.model.System;

import java.util.List;

public class ResponseGetSystems {
    private List<System> systems;

    public List<System> getSystems() {
        return systems;
    }

    public void setSystems(List<System> systems) {
        this.systems = systems;
    }
}
