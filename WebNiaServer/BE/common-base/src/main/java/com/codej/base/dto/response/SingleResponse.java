package com.codej.base.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponse<T> extends ResultResponse<T> {
    public SingleResponse() {
    }

    public static SingleResponse<?> createResult(SingleResponse<?> result, Boolean encrypt) {
        if (encrypt != null && encrypt) {
            SingleResponse<String> encryptResult = new SingleResponse<String>();
            encryptResult.setResult(encrypt((result).getResult()));
            encryptResult.setEncrypt(true);
            return encryptResult;
        }
        return result;
    }
}
