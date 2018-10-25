package com.nrdc.policeHamrah.test;

import com.nrdc.policeHamrah.helper.Encryption;
import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestActiveUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestDeActiveUser;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

@Path("/test")//sag toot!
public class TestWebServices {
    private final static String baseUrl = "http://127.0.0.1:7001/policeHamrah/api";
    private static Logger logger = Logger.getLogger(TestWebServices.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {

//        String testDeActivateUserServiceResult = testDeActivateUserService();
        String testActivateUserServiceResult = testActivateUserService();
//        stringBuilder.append(testDeActivateUserServiceResult);
        return testActivateUserServiceResult;
    }

    @Path("/2")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test2() throws NoSuchAlgorithmException {

        return Encryption.encryptOrNull("key", "سلام");
    }

    @Path("/3")
    @GET
    @Produces(MediaType.TEXT_PLAIN + "; charset=UTF-8")
    public String test3() throws NoSuchAlgorithmException {

        return Encryption.decryptOrNull("key", Encryption.encryptOrNull("key", "سلام"));
    }

    @Path("/4")
    @GET
    @Produces(MediaType.TEXT_PLAIN + "; charset=UTF-8")
    public String test4() throws NoSuchAlgorithmException {

        return "سلام";
    }


    private String testActivateUserService() {
        try {

            String result = ("Test Activate User Service");
            RequestActiveUser requestActiveUser = new RequestActiveUser();
            requestActiveUser.setFkUserId(1L);
            requestActiveUser.setFkSystemId(4L);
            StandardResponse response = sendRequest(baseUrl + "/user/activate", "POST", requestActiveUser, "key", "token");
            logger.info(response);
            result += handleResponse(response);

            return result;
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }

    private String handleResponse(StandardResponse response) {
        String result = "";
        if (response.getResultCode() == 1) {
            result += (": Passed ");
        } else {
            result += (": failed With reason :{");
            result += (response.getResultMessage());
            result += ("}");
        }
        return result;
    }

    private String testDeActivateUserService() {
        try {
            String result = ("Test DeActivate User Service");
            RequestDeActiveUser requestDeActiveUser = new RequestDeActiveUser();
            requestDeActiveUser.setFkUserId(1L);
            requestDeActiveUser.setFkSystemId(4L);
            StandardResponse response = sendRequest(baseUrl + "/user/deActivate", "POST", requestDeActiveUser, "key", "token");
            logger.info(response);
            result += handleResponse(response);
            return result;
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }


    private StandardResponse sendRequest(String path, String method, Object request, String key, String token) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            EncryptedRequest encryptedRequest = new EncryptedRequest();
            encryptedRequest.setToken(token);
            String data = Encryption.encryptOrNull(key, objectMapper.writeValueAsString(request));
            encryptedRequest.setData(data);
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            String input = new ObjectMapper().writeValueAsString(encryptedRequest);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            wr.write(input);
            wr.flush();
            wr.close();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error Code : " + connection.getResponseCode());
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder output = new StringBuilder();
            int c;
            while ((c = bufferedReader.read()) != -1)
                output.append((char) c);
            String result = output.toString();
            bufferedReader.close();
            String encryptedData = objectMapper.readValue(result, EncryptedResponse.class).getData();
            StandardResponse standardResponse = objectMapper.readValue(Encryption.decryptOrNull(key, encryptedData), StandardResponse.class);
            return standardResponse;

        } catch (Exception ex) {
            return null;
        }

    }

    private String sendGetRequest(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error in Calling rest service");
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder output = new StringBuilder();
            int c;
            while ((c = bufferedReader.read()) != -1)
                output.append((char) c);
            return output.toString();

        } catch (Exception ex) {
            return null;
        }

    }
}
