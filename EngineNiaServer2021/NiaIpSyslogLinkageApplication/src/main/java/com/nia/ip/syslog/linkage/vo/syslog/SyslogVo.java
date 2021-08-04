package com.nia.ip.syslog.linkage.vo.syslog;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Scope(value = "prototype")
public class SyslogVo implements Serializable {
    @JsonProperty("msg")
    private String msg;

    @JsonProperty("msg")
    public String getMsg() {
        return msg;
    }

    @JsonProperty("msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
