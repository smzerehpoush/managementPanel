package com.nrdc.policeHamrah.helper;


import com.nrdc.policeHamrah.jsonModel.EncryptedRequest;
import com.nrdc.policeHamrah.jsonModel.EncryptedResponse;
import com.nrdc.policeHamrah.model.dao.UserDao;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.security.NoSuchAlgorithmException;


public class Encryption {
    private static Logger logger = Logger.getLogger(Encryption.class.getName());

    private static se.simbio.encryption.Encryption getEncryption(String key) throws NoSuchAlgorithmException {
        return buildEncryption(key, "JVAaVhAiddKAaghraikhmaini");
    }

    private static se.simbio.encryption.Encryption buildEncryption(String key, String salt) throws NoSuchAlgorithmException {
        se.simbio.encryption.Encryption.Builder encryptionBuilder = new se.simbio.encryption.Encryption.Builder()
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
        se.simbio.encryption.Encryption encryption = getEncryption(key);
        assert encryption != null;
        return encryption.encryptOrNull(data).replace("\n", "");
    }

    public static String decryptOrNull(String key, String data) throws NoSuchAlgorithmException {
        se.simbio.encryption.Encryption encryption = getEncryption(key);
        assert encryption != null;
        return encryption.decryptOrNull(data);
    }

    public static String decryptRequest(EncryptedRequest request) throws Exception {

        String key;
        if (request.getToken().equals("Android"))
            key = "Android";
        else
            key = UserDao.getKey(request.getToken()).getKey();
        String decrypted = decryptOrNull(key, request.getData());
        logger.info(decrypted);
        return decrypted;
    }


    public static String decryptPassword(String key, String data) throws Exception {
        return decryptOrNull(key, data);
    }

    public static EncryptedResponse encryptResponse(Object response) {
        return encryptResponse(Constants.DEFAULT_KEY, response);
    }

    public static EncryptedResponse encryptResponse(String key, Object response) {
        EncryptedResponse encryptedResponse = new EncryptedResponse();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseText = objectMapper.writeValueAsString(response);
            logger.info(responseText);
            String strEncrypted = encryptOrNull(key, responseText);
            encryptedResponse.setData(strEncrypted);
            return encryptedResponse;
        } catch (Exception e) {
            return null;
        }
    }

}
