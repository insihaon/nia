package com.nia.korenrca.service.cipher.rsa;

import javax.crypto.Cipher;
import java.io.Serializable;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;


public class RSA {
    private static String RSA_INSTANCE = "RSA"; // rsa transformation

    /**
     * rsa 공개키, 개인키 생성
     */
    public static RSAKey createRsa() {
        RSAKey rsaKey = new RSAKey();
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(RSA.RSA_INSTANCE);
            generator.initialize(2048);

            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance(RSA.RSA_INSTANCE);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            rsaKey.setPrivateKey(privateKey);// RSA 개인키

            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

            rsaKey.setPublicKeyModulus(publicKeyModulus); // rsa modulus
            rsaKey.setPublicKeyExponent(publicKeyExponent); // rsa exponent
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rsaKey;
    }

    public static String decryptRsa(RSAKey rsaKey, String encryptText) throws Exception {
        // Iterator<String> keys = data.getProperties().keySet().iterator();
        // while (keys.hasNext()) {
        // String key = keys.next();
        // String value = data.get(key);
        // }
        PrivateKey privateKey = rsaKey.getPrivateKey();
        // 복호화
        String text = decryptRsa(privateKey, encryptText);
        return text;
    }

    /**
     * 복호화
     *
     * @param privateKey
     * @param securedValue
     * @return
     * @throws Exception
     */
    private static String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA.RSA_INSTANCE);
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }

    /**
     * 16진 문자열을 byte 배열로 변환한다.
     *
     * @param hex
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }

    public static class RSAKey implements Serializable {

        private PrivateKey privateKey;
        private String publicKeyModulus;
        private String publicKeyExponent;

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(PrivateKey privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKeyModulus() {
            return publicKeyModulus;
        }

        public void setPublicKeyModulus(String publicKeyModulus) {
            this.publicKeyModulus = publicKeyModulus;
        }

        public String getPublicKeyExponent() {
            return publicKeyExponent;
        }

        public void setPublicKeyExponent(String publicKeyExponent) {
            this.publicKeyExponent = publicKeyExponent;
        }
    }

}
