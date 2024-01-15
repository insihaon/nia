package com.codej.base.utils;

import java.util.HashMap;

import org.json.JSONObject;

import com.codej.base.utils.cipher.tea.TEA;

public class EncryptUtil {

    private EncryptUtil() {
    }

    public static String encrypt(String value) {
        String uuid = TEA.generateUUID();
        TEA tea = new TEA(uuid);
        return uuid + tea.encrypt(value);
    }

    public static String decryptText(String keyEncData) {
        try {
            String teaKey = keyEncData.substring(0, 16);
            String encryptValue = keyEncData.substring(16, keyEncData.length());
            TEA tea = new TEA(teaKey);
            return tea.decrypt(encryptValue);
        } catch (Exception e) {
            return null;
        }
    }

    public static HashMap<String, Object> decrypt(String keyEncData) {
        String value = decryptText(keyEncData);
        JSONObject json = new JSONObject(value);
        return (HashMap<String, Object>) json.toMap();
    }
}