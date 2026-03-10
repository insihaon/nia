package com.codej.base.utils;

import java.util.HashMap;

import com.codej.base.dto.model.Data;
import com.codej.base.exception.CServiceIncorrectUse;
import com.codej.base.property.GlobalConstants;

public class CryptUtil {

    private CryptUtil() {
    }

    public static HashMap<String, Object> decryptToMap(HashMap<String, Object> param, Boolean encrypt) {
        HashMap<String, Object> data = null;
        if (encrypt == true) {
            String endata = (String) param.get(GlobalConstants.Common.DATA);
            data = EncryptUtil.decrypt(endata);
        }

        if (data == null) {
            data = new HashMap<String, Object>(param);
        }

        return data;
    }

    public static HashMap<String, Object> decryptToMap(HashMap<String, Object> param) {
        try {
            String encryptText = (String) param.get(GlobalConstants.Common.ENCRYPT);
            Boolean encrypt = Boolean.parseBoolean(EncryptUtil.decryptText(encryptText));
            return decryptToMap(param, encrypt);
        } catch (Exception e) {
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + "decrypt 실패");
        }
    }

    public static Data decryptToData(HashMap<String, Object> param) {
        HashMap<String, Object> map = decryptToMap(param);
        Data data = new Data(map);
        return data;
    }
}
