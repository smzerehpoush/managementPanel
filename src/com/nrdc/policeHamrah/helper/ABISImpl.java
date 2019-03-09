package com.nrdc.policeHamrah.helper;

import com.google.gson.Gson;
import com.nrdc.policeHamrah.model.dao.ConstantDao;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ABISImpl {
    private final static String username = "mobileClient";
    private final static String password = "422@Mobile";

    public String sendRequest(Object request, String serviceName) throws Exception {
        String servicePath = ConstantDao.getConstant("ABIS_BASE_PATH") + ConstantDao.getConstant(serviceName);
        return callPostService(servicePath, request);
    }

    public String sendRequest(String serviceName, String pathParam) throws Exception {
        String servicePath = ConstantDao.getConstant("ABIS_BASE_PATH") + ConstantDao.getConstant(serviceName);
        return callGetService(servicePath + "/" + pathParam);
    }


    private String callPostService(String path, Object object) throws Exception {
        URL obj = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        BASE64Encoder enc = new BASE64Encoder();
        String userPass = username + ":" + password;
        String encodedAuthorization = enc.encode(userPass.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(new Gson().toJson(object));
        wr.flush();
        wr.close();
        int responceCode = connection.getResponseCode();
        System.out.println(responceCode);
        //receiving response message
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer resp = new StringBuffer();
        while ((inputLine = bufferedReader.readLine()) != null) {
            resp.append(inputLine);
        }
        bufferedReader.close();
        return (resp.toString());
    }

    private String callGetService(String path) throws Exception {
        URL obj = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        BASE64Encoder enc = new BASE64Encoder();
        String userPass = username + ":" + password;
        String encodedAuthorization = enc.encode(userPass.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        connection.setDoOutput(true);
        int responceCode = connection.getResponseCode();
        //receiving response message
        try {
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                resp.append(inputLine);
            }
            bufferedReader.close();
            return (resp.toString());
        }catch (Exception ex){
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                resp.append(inputLine);
            }
            bufferedReader.close();
            return (resp.toString());
        }

    }
}
