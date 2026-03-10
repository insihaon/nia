package com.codej.base.dto.response;

import com.codej.base.utils.EncryptUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    protected boolean success = true;
    protected boolean encrypt = false;
    private String[] sql;
    private Integer total;

    public BaseResponse() {
    }

    public static String encrypt(Object result) {
        Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // 원하는 형식 설정
            .create();
        String json = gson.toJson(result);
        return EncryptUtil.encrypt(json);
    }
}
