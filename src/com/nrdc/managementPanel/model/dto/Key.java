package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.model.dao.KeyDAO;

import java.util.UUID;

public class Key extends KeyDAO {

    public Key() {
    }

    public Key(User user, System system) throws Exception {
        user.checkKey(system);
        this.setKey(UUID.randomUUID().toString());
        this.setFkSystemId(system.getId());
        this.setFkUserId(user.getId());
    }

}
