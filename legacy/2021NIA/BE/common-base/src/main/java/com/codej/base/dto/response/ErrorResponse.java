package com.codej.base.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends ResultResponse<Boolean> {
    private String message;
    private String detailMessage;

    public ErrorResponse() {
        this.setResult(false);
    }
}
