package laboratorio.app.auth;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    public static String encryptToMD5(String value) {
        if (value == null)
            return null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(value.getBytes());

            StringBuffer buffer = new StringBuffer();
            for (byte bytes : digest)
                buffer.append(String.format("%02x", bytes & 0xff));

            return new String(buffer);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
   }
}
