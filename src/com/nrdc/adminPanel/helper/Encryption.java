package com.nrdc.adminPanel.helper;

import com.nrdc.adminPanel.jsonModel.EncryptedRequest;
import com.nrdc.adminPanel.jsonModel.EncryptedResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger = Logger.getLogger(Encryption.class.getName());

    private static se.simbio.encryption.Encryption.Builder encryptionBuilder = new se.simbio.encryption.Encryption.Builder()
            .setIv(new byte[16])
            .setKey("Android")
            .setSalt("JVAaVhAiddKAaghraikhmaini")
            .setKeyLength(128)
            .setKeyAlgorithm("AES")
            .setCharsetName("UTF8")
            .setIterationCount(16)
            .setDigestAlgorithm("SHA1")
            .setBase64Mode(0)
            .setAlgorithm("AES/CBC/PKCS5Padding")
            .setSecureRandomAlgorithm("SHA1PRNG")
            .setSecretKeyType("PBKDF2WithHmacSHA1");
    private static se.simbio.encryption.Encryption encryption;

    private static se.simbio.encryption.Encryption getEncryption() {
        try {
            return encryptionBuilder.build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String encryptOrNull(String data) {
        encryption = getEncryption();
        assert encryption != null;
        return encryption.encryptOrNull(data);
    }

    private static String decryptOrNull(String data) {
        encryption = getEncryption();
        assert encryption != null;
        return encryption.decryptOrNull(data);
    }

    public static EncryptedResponse encryptResponse(Object response) throws IOException {
        EncryptedResponse encryptedResponse = new EncryptedResponse();
        String strEncrypted = null;
        try {
            strEncrypted = encryptOrNull(objectMapper.writeValueAsString(response));
        } catch (IOException e) {
            logger.error("error in encrypting response");
            logger.error(e.getMessage(), e);
            throw e;
        }
        encryptedResponse.setData(strEncrypted.replace("\n", ""));
        return encryptedResponse;
    }

    public static String decryptRequest(EncryptedRequest request) throws Exception {
        String decrypted = decryptOrNull(request.getData());
        if (decrypted == null || decrypted.isEmpty()) {
            throw new Exception("error in decrypted message.\nmessage is null");
        }
        return decrypted;
    }
}
