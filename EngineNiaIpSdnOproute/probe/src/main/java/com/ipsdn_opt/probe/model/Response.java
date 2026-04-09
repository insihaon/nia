package com.ipsdn_opt.probe.model;

import lombok.Data;

@Data
public class Response {
    private boolean status;
    private String message;
    private Object data;

    public Response(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
