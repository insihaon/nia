package com.codej.web.vo;

import java.io.Serializable;

public class EncryptedVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean encrypt;
    private Object result;

    public EncryptedVo(boolean encrypt, Object result) {
        this.encrypt = encrypt;
        this.result = result;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
