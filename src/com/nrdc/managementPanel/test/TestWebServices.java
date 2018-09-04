package com.nrdc.managementPanel.test;

import com.nrdc.managementPanel.helper.Encryption;
import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestActiveUser;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestDeActiveUser;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Path("/test")
public class TestWebServices {
    private final static String baseUrl = "http://127.0.0.1:7001/managementPanel/api";
    private static Logger logger = Logger.getLogger(TestWebServices.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN + "; charset=UTF-8")
    public Response test() {
        StringBuilder stringBuilder = new StringBuilder();
        String testDeActivateUserServiceResult = testDeActivateUserService();
        String testActivateUserServiceResult = testActivateUserService();
        stringBuilder.append(testDeActivateUserServiceResult);
        stringBuilder.append(testActivateUserServiceResult);
        Response finalResponse = Response.status(200).entity(stringBuilder.toString()).build();
        return finalResponse;
    }

    private String testActivateUserService() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Test Activate User Service");
            RequestActiveUser requestActiveUser = new RequestActiveUser();
            requestActiveUser.setFkUserId(1L);
            requestActiveUser.setFkSystemId(4L);
            StandardResponse response = sendPostRequest(baseUrl + "/user/activate", requestActiveUser, "key", "token");
            logger.info(response);
            if (response.getResultCode() == 1) {
                stringBuilder.append(": Passed ");
            } else {
                stringBuilder.append(": failed With reason :{")
                        .append(response.getResultMessage())
                        .append("}");
            }

            return stringBuilder.toString();
        } catch (Exception ex) {
            return null;
        }

    }

    private String testDeActivateUserService() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Test DeActivate User Service");
            RequestDeActiveUser requestDeActiveUser = new RequestDeActiveUser();
            requestDeActiveUser.setFkUserId(1L);
            StandardResponse response = sendPostRequest(baseUrl + "/user/deActivate", requestDeActiveUser, "key", "token");
            logger.info(response);
            if (response.getResultCode() == 1) {
                stringBuilder.append(": Passed ");
            } else {
                stringBuilder.append(": failed With reason :{")
                        .append(response.getResultMessage())
                        .append("}");
            }

            return stringBuilder.toString();
        } catch (Exception ex) {
            return null;
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
            String encryptedData =result.substring(result.indexOf(":\"")+2,result.indexOf("\"}",result.indexOf(":\"")));
            StandardResponse standardResponse = objectMapper.readValue(Encryption.decryptOrNull(key,encryptedData), StandardResponse.class);
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
