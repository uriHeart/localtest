package com.argo.restapi.auth;

import com.argo.restapi.exception.DecryptFailException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.springframework.stereotype.Service;

@Service
public class RsaDecrypter {

    public String decryptRsa(String credentials, String privateKey) throws DecryptFailException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey(privateKey));
            byte[] passwordByte = Base64.getDecoder().decode(credentials);
            byte[] bytes = cipher.doFinal(passwordByte);
            return new String(bytes);
        } catch (Exception e) {
            throw new DecryptFailException(e);
        }
    }

    private PrivateKey getRsaPrivateKey(String base64PrivateKey) throws Exception {
        byte[] privateKey = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
