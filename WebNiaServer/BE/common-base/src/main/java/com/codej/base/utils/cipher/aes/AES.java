package com.codej.base.utils.cipher.aes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;


public class AES {

  public static void main(String[] args) {
    String aesencrypt = encrypt("hello12", "rms-web");
    String aesdecrypt = decrypt(aesencrypt, "rms-web");
    System.out.println(aesencrypt);
    System.out.println(aesdecrypt);
  }

  public static String encrypt(String password, String strKey) {
    try {
      byte[] keyBytes = Arrays.copyOf(strKey.getBytes("ASCII"), 16);

      SecretKey key = new SecretKeySpec(keyBytes, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, key);

      byte[] cleartext = password.getBytes("UTF-8");
      byte[] ciphertextBytes = cipher.doFinal(cleartext);

      return new String(Hex.encodeHex(ciphertextBytes));

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String decrypt(String passwordhex, String strKey) {
    try {
      byte[] keyBytes = Arrays.copyOf(strKey.getBytes("ASCII"), 16);

      SecretKey key = new SecretKeySpec(keyBytes, "AES");
      Cipher decipher = Cipher.getInstance("AES");

      decipher.init(Cipher.DECRYPT_MODE, key);

      char[] cleartext = passwordhex.toCharArray();

      byte[] decodeHex = Hex.decodeHex(cleartext);

      byte[] ciphertextBytes = decipher.doFinal(decodeHex);

      return new String(ciphertextBytes);

    } catch (Exception e) {
      e.getMessage();
    }
    return null;
  }

}
