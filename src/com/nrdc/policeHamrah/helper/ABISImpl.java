package com.nrdc.policeHamrah.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nrdc.policeHamrah.model.dao.ConstantDao;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import sun.misc.BASE64Encoder;

import java.util.Date;

public class ABISImpl {
    private final static String username = "mobileClient";
    private final static String password = "422@Mobile";

    private static String callGetService(String path) {
        WebResource.Builder webResourceBuilder = createWebResourceBuilder(path);
        ClientResponse response = webResourceBuilder.get(ClientResponse.class);
        return response.getEntity(String.class);
    }

    private static WebResource.Builder createWebResourceBuilder(String path) {
        Client client = Client.create();
        WebResource webResource = client
                .resource(path);
        BASE64Encoder enc = new BASE64Encoder();
        String userPass = username + ":" + password;
        String encodedAuthorization = enc.encode(userPass.getBytes());

        return webResource.type("application/json")
                .header("Authorization", "Basic " + encodedAuthorization);

    }

    private static String callPostService(String path, Object object) {
        WebResource.Builder webResourceBuilder = createWebResourceBuilder(path);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateLongFormatTypeAdapter()).create();
        ClientResponse response = webResourceBuilder.post(ClientResponse.class, gson.toJson(object));
        return response.getEntity(String.class);
    }

    public String sendRequest(Object request, String serviceName) throws Exception {
        String servicePath = ConstantDao.getConstant("ABIS_BASE_PATH") + ConstantDao.getConstant(serviceName);
        return callPostService(servicePath, request);
    }

    public String sendRequest(String serviceName, String pathParam) throws Exception {
        String servicePath = ConstantDao.getConstant("ABIS_BASE_PATH") + ConstantDao.getConstant(serviceName);
        return callGetService(servicePath + "/" + pathParam);
    }

}
