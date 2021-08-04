package com.nia.tsdn.service.result.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component()
@Scope(value = "prototype")
public class ReservedResultVo implements Serializable {
    private String serviceId;
    private String message;
    private Boolean isSuccess;
    private String requestUrl;
    private int reservedNum;
}
