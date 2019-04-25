package com.nrdc.policeHamrah.helper;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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
        Client client = Client.create();
        WebResource webResource = client
                .resource(path);
        Gson gson = MyGsonBuilder.build();
        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, gson.toJson(object));

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        return response.getEntity(String.class);
    }
}
