package com.nrdc.managementPanel.helper;


import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.model.Token;
import com.nrdc.managementPanel.model.User;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


public class Encryption {
    private static se.simbio.encryption.Encryption.Builder encryptionBuilder;
    private static se.simbio.encryption.Encryption encryption;

    private static se.simbio.encryption.Encryption getEncryption(String key) throws NoSuchAlgorithmException {
        return buildEncryption(key, "JVAaVhAiddKAaghraikhmaini");
    }

    private static se.simbio.encryption.Encryption getEncryption(String key, String salt) throws NoSuchAlgorithmException {
        return buildEncryption(key, "JVAaVhAiddKAaghraikhmaini");
    }

    private static se.simbio.encryption.Encryption buildEncryption(String key, String salt) throws NoSuchAlgorithmException {
        encryptionBuilder = new se.simbio.encryption.Encryption.Builder()
                .setIv(new byte[16])
                .setKey(key)
                .setSalt(salt)
                .setKeyLength(128)
                .setKeyAlgorithm("AES")
                .setCharsetName("UTF8")
                .setIterationCount(16)
                .setDigestAlgorithm("SHA1")
                .setBase64Mode(0)
                .setAlgorithm("AES/CBC/PKCS5Padding")
                .setSecureRandomAlgorithm("SHA1PRNG")
                .setSecretKeyType("PBKDF2WithHmacSHA1");
        return encryptionBuilder.build();
    }

    public static String encryptOrNull(String key, String data) throws NoSuchAlgorithmException {
        encryption = getEncryption(key);
        assert encryption != null;
        return encryption.encryptOrNull(data);
    }

    public static String decryptOrNull(String key, String data) throws NoSuchAlgorithmException {
        encryption = getEncryption(key);
        assert encryption != null;
        return encryption.decryptOrNull(data);
    }

    public static String decryptRequest(EncryptedRequest request) throws Exception {
        String key = Database.getUserKey(request.getToken(), SystemNames.MANAGEMENT_PANEL).getKey();
        return decryptOrNull(key, request.getData());
    }

    public static EncryptedResponse encryptResponse(String key, Object response) throws IOException, NoSuchAlgorithmException {
        EncryptedResponse encryptedResponse = new EncryptedResponse();
        encryption = getEncryption(key);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String strEncrypted = encryption.encryptOrNull(objectMapper.writeValueAsString(response));
            encryptedResponse.setData(strEncrypted.replace("\n", ""));
            return encryptedResponse;
        } catch (IOException e) {
            throw e;
        }
    }

}
