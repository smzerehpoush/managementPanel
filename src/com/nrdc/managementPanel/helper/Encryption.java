package com.nrdc.managementPanel.helper;


import com.nrdc.managementPanel.jsonModel.EncryptedRequest;
import com.nrdc.managementPanel.jsonModel.EncryptedResponse;
import com.nrdc.managementPanel.model.dao.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class Encryption {
    private static Logger logger = Logger.getLogger(Encryption.class.getName());
    private static se.simbio.encryption.Encryption.Builder encryptionBuilder;
    private static se.simbio.encryption.Encryption encryption;

    private static se.simbio.encryption.Encryption getEncryption(String key) throws NoSuchAlgorithmException {
        return buildEncryption(key, "JVAaVhAiddKAaghraikhmaini");
    }

    private static se.simbio.encryption.Encryption getEncryption(String key, String salt) throws NoSuchAlgorithmException {
        return buildEncryption(key, salt);
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
        return encryption.encryptOrNull(data).replace("\n", "");
    }

    public static String decryptOrNull(String key, String data) throws NoSuchAlgorithmException {
        encryption = getEncryption(key);
        assert encryption != null;
        return encryption.decryptOrNull(data);
    }

    private static String correctUTF8String(String oldData) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : oldData.toCharArray()) {
            switch (c) {
                case 'ا':
                    stringBuilder.append('À');
                    break;

                case 'ب':
                    stringBuilder.append('Á');
                    break;

                case 'ة':
                    stringBuilder.append('Â');
                    break;

                case 'ت':
                    stringBuilder.append('Ã');
                    break;

                case 'ث':
                    stringBuilder.append('Ä');
                    break;

                case 'ج':
                    stringBuilder.append('Å');
                    break;

                case 'ح':
                    stringBuilder.append('Æ');
                    break;

                case 'خ':
                    stringBuilder.append('Ç');
                    break;

                case 'د':
                    stringBuilder.append('È');
                    break;

                case 'ذ':
                    stringBuilder.append('É');
                    break;

                case 'ر':
                    stringBuilder.append('Ê');
                    break;

                case 'ز':
                    stringBuilder.append('Ë');
                    break;

                case 'س':
                    stringBuilder.append('Ì');
                    break;

                case 'ش':
                    stringBuilder.append('Í');
                    break;

                case 'ص':
                    stringBuilder.append('Î');
                    break;

                case 'ض':
                    stringBuilder.append('Ï');
                    break;

                case 'ط':
                    stringBuilder.append('Ð');
                    break;

                case 'ظ':
                    stringBuilder.append('Ñ');
                    break;

                case 'ع':
                    stringBuilder.append('Ò');
                    break;

                case 'غ':
                    stringBuilder.append('Ó');
                    break;

                case 'ػ':
                    stringBuilder.append('Ô');
                    break;

                case 'ؼ':
                    stringBuilder.append('Õ');
                    break;

                case 'ؽ':
                    stringBuilder.append('Ö');
                    break;

                case 'ؾ':
                    stringBuilder.append('×');
                    break;

                case 'ؿ':
                    stringBuilder.append('Ø');
                    break;

                case 'ف':
                    stringBuilder.append('Ú');
                    break;

                case 'ق':
                    stringBuilder.append('Û');
                    break;

                case 'ك':
                    stringBuilder.append('Ü');
                    break;

                case 'ل':
                    stringBuilder.append('Ý');
                    break;

                case 'م':
                    stringBuilder.append('Þ');
                    break;

                case 'ن':
                    stringBuilder.append('ß');
                    break;

                case 'ه':
                    stringBuilder.append('à');
                    break;

                case 'و':
                    stringBuilder.append('á');
                    break;

                case 'ى':
                    stringBuilder.append('â');
                    break;

                case 'ي':
                    stringBuilder.append('ã');
                    break;
                default:
                    stringBuilder.append(c);
                    break;

            }
        }
        return stringBuilder.toString();
    }

    private static String reverseUTF8String(String oldData) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : oldData.toCharArray()) {
            switch (c) {
                case 'À':
                    stringBuilder.append('ا');
                    break;

                case 'Á':
                    stringBuilder.append('ب');
                    break;

                case 'Â':
                    stringBuilder.append('ة');
                    break;

                case 'Ã':
                    stringBuilder.append('ت');
                    break;

                case 'Ä':
                    stringBuilder.append('ث');
                    break;

                case 'Å':
                    stringBuilder.append('ج');
                    break;

                case 'Æ':
                    stringBuilder.append('ح');
                    break;

                case 'Ç':
                    stringBuilder.append('خ');
                    break;

                case 'È':
                    stringBuilder.append('د');
                    break;

                case 'É':
                    stringBuilder.append('ذ');
                    break;

                case 'Ê':
                    stringBuilder.append('ر');
                    break;

                case 'Ë':
                    stringBuilder.append('ز');
                    break;

                case 'Ì':
                    stringBuilder.append('س');
                    break;

                case 'Í':
                    stringBuilder.append('ش');
                    break;

                case 'Î':
                    stringBuilder.append('ص');
                    break;

                case 'Ï':
                    stringBuilder.append('ض');
                    break;

                case 'Ð':
                    stringBuilder.append('ط');
                    break;

                case 'Ñ':
                    stringBuilder.append('ظ');
                    break;

                case 'Ò':
                    stringBuilder.append('ع');
                    break;

                case 'Ó':
                    stringBuilder.append('غ');
                    break;

                case 'Ô':
                    stringBuilder.append('ػ');
                    break;

                case 'Õ':
                    stringBuilder.append('ؼ');
                    break;

                case 'Ö':
                    stringBuilder.append('ؽ');
                    break;

                case '×':
                    stringBuilder.append('ؾ');
                    break;

                case 'Ø':
                    stringBuilder.append('ؿ');
                    break;

                case 'Ú':
                    stringBuilder.append('ف');
                    break;

                case 'Û':
                    stringBuilder.append('ق');
                    break;

                case 'Ü':
                    stringBuilder.append('ك');
                    break;

                case 'Ý':
                    stringBuilder.append('ل');
                    break;

                case 'Þ':
                    stringBuilder.append('م');
                    break;

                case 'ß':
                    stringBuilder.append('ن');
                    break;

                case 'à':
                    stringBuilder.append('ه');
                    break;

                case 'á':
                    stringBuilder.append('و');
                    break;

                case 'â':
                    stringBuilder.append('ى');
                    break;

                case 'ã':
                    stringBuilder.append('ي');
                    break;
                default:
                    stringBuilder.append(c);
                    break;

            }
        }
        return stringBuilder.toString();
    }


    public static String decryptRequest(EncryptedRequest request) throws Exception {
        String key = User.getKey(request.getToken(), SystemNames.MANAGEMENT_PANEL).getKey();
        String decrypted = decryptOrNull(key, request.getData());
        logger.debug(decrypted);
        return decrypted;
    }

    public static String decryptResponse(EncryptedResponse response, String key) throws Exception {
        String decrypted = decryptOrNull(key, response.getData());
        logger.debug(decrypted);
        return decrypted;
    }

    public static String decryptPassword(String key, String data) throws Exception {
        return decryptOrNull(key, data);
    }

    public static EncryptedResponse encryptResponse(String key, Object response) throws IOException, NoSuchAlgorithmException {
        EncryptedResponse encryptedResponse = new EncryptedResponse();
        encryption = getEncryption(key);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String strEncrypted = encryptOrNull(key, objectMapper.writeValueAsString(response));
            encryptedResponse.setData(strEncrypted);
            return encryptedResponse;
        } catch (IOException e) {
            throw e;
        }
    }

}
