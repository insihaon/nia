package com.codej.base.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultResponse<T> extends BaseResponse {
    private T result;

    public ResultResponse() {
    }

    public static ResultResponse<?> createResult(ResultResponse<?> result, Boolean encrypt) {
        if (encrypt != null && encrypt) {
            ResultResponse<String> encryptResult = new ResultResponse<String>();
            encryptResult.setResult(encrypt((result).getResult()));
            encryptResult.setEncrypt(true);
            encryptResult.setTotal(result.getTotal());;
            return encryptResult;
        }
        return result;
    }
}
