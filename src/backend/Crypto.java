package backend;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Crypto class to encrypt and decrypt the message with given secret key this
 * class is not instanceiable
 */
public final class Crypto {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String userKey) {
        try {
            MessageDigest sha = null;
            key = userKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String msg, String secret) {

        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }

        return null;

    }

    public static String decrypt(String encMsg, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encMsg)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }

        return null;

    }
    // ---> Test <----
    // public static void main(String args[]) {
    // String key = "ISHANNNNASNKJASJLASJLKSJKASJ";
    // String msg = "Ishan is hero";
    // String encMsg = Crypto.encrypt(msg, key);
    // String decMsg = Crypto.decrypt(encMsg, key);
    // System.out.println(msg);
    // System.out.println(encMsg);
    // System.out.println(decMsg);
    // }
}
