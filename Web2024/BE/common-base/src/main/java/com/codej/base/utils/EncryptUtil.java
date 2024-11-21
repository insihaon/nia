package com.codej.base.utils;

import java.util.HashMap;

import org.json.JSONObject;

import com.codej.base.utils.cipher.tea.TEA;

public class EncryptUtil {
    private static final int idx = 3;
    private static final int len = 16;
    private EncryptUtil() {
    }

    private static String uuid() {
        return TEA.generateUUID(len);
    }

    public static String encrypt(String value) {
        String uuid = uuid();
        TEA tea = new TEA(uuid);
        String data = tea.encrypt(value);
        return new StringBuilder(data).insert(Math.min(data.length(), idx), uuid).toString();
    }

    public static String decryptText(String keyEncData) {
        try {
            int s = idx;
            int e = s + len;
            String teaKey = keyEncData.substring(s, e);
            String encryptValue = keyEncData.substring(0, s) + keyEncData.substring(e);
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