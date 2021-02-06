package laboratorio.app.auth;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Encryptor {

    public static String encrypt(String value) {
        if (value == null)
            return null;

        byte[] salt = new byte[16];
        KeySpec spec = new PBEKeySpec(value.toCharArray(), salt, 65536, 128);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] encode = factory.generateSecret(spec).getEncoded();
            return new String(encode);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
