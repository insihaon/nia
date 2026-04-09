package com.codej.base.exception;

import lombok.Data;

@Data
public class CBaseException extends RuntimeException {

    String code;
    String codeKey;
    String messageKey;
    String detailMessageKey;
    String detailMessage;
    
    public CBaseException() {
        this.code = "unKnown";
    }    

    public CBaseException(String key) {
        this.code = key;
        this.codeKey = String.format("%s.code", key);
        this.messageKey = String.format("%s.message", key);
        this.detailMessageKey = String.format("%s.message_detail", key);
    }

    public CBaseException(String key, String detailMessage) {
        this(key);
        this.detailMessage = detailMessage;
    }
}
