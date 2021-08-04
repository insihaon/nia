package com.nia.tsdn.service.result.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class ServiceCreateResultVo implements Serializable {
    private String serviceId;
    private String message;
    private Boolean isSuccess;
    private String requestUrl;
}
