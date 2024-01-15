package com.codej.base.dto.response;

import com.codej.base.utils.EncryptUtil;
import com.codej.base.utils.GsonFactory;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    protected boolean success = true;
    protected boolean encrypt = false;
    private String sql;
    private Integer total;

    public BaseResponse() {
    }

    public static String encrypt(Object result) {
        Gson gson = GsonFactory.build();
        String json = gson.toJson(result);
        return EncryptUtil.encrypt(json);
    }
}
