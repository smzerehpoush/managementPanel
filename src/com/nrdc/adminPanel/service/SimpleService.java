package com.nrdc.adminPanel.service;

import com.nrdc.adminPanel.impl.Database;
import org.codehaus.jackson.map.ObjectMapper;

public class SimpleService {
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected Database database = new Database();

}
