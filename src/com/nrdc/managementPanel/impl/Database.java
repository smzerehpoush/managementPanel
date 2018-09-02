package com.nrdc.managementPanel.impl;


import com.nrdc.managementPanel.exceptions.NotValidTokenException;
import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.model.Key;
import com.nrdc.managementPanel.model.System;
import com.nrdc.managementPanel.model.Token;
import com.nrdc.managementPanel.model.User;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.List;

public class Database {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("managementPanelJPA");
    private static Logger logger = Logger.getLogger(Database.class.getName());

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }


}