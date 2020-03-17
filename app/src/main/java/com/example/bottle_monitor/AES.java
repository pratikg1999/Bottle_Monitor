package com.example.bottle_monitor;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    /**
     * This instance specifies a secret key in a provider-independent fashion.
     */
    private static SecretKeySpec secretKey;
    public static String SECRET_KEY = "SECRET KEY";
//    {
//        setKey("SECRET SALT");
//    }

    /**
     * Used to store the myKey in the form of byte array required for encryption / decryption.
     */
    private static byte[] key;

    /**
     * Sets the value of the byte array <strong>key</strong>
     * <p>
     * The myKey value is converted to byte array and only the first 16 elements of the array are taken.
     * The remaining elements are removed.This is required to initialize the secretKey;
     *
     * @param myKey The unique key used for encryption / decryption.
     */
    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts the given string.
     * <p>
     * It first sets the value of key and secretKey by calling the setKey() function.
     * It then encrypts the data using the secretKey.
     *
     * @param strToEncrypt It represents the string to be encrypted.
     * @param secret It is used to generate the key and secretKey.
     * @return the eccypted String
     */
    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")), Base64.DEFAULT);
//            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    /**
     * Decrypts the given string.
     * <p>
     * It first sets the value of key and secretKey by calling the setKey() function.
     * It then encrypts the data using the secretKey.
     *
     * @param strToDecrypt It represents the string to be decrypted.
     * @param secret It is used to generate the key and secretKey.
     * @return the decrypted String
     */
    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
