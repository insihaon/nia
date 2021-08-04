package com.nia.ip.syslog.linkage.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class ResultVo implements Serializable {
    private int res_code;
    private String res_msg;
}
