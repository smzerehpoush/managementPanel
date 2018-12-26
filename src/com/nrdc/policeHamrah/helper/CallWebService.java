package com.nrdc.policeHamrah.helper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class CallWebService {
    public static String callGetService(String path) {
        Client client = Client.create();
        WebResource webResource = client
                .resource(path);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        return response.getEntity(String.class);
    }

    public static String callPostService(String path, Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = Client.create();
        WebResource webResource = client
                .resource(path);

        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, objectMapper.writeValueAsString(object));

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        return response.getEntity(String.class);
    }
}
