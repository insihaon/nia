package com.nia.engine.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class UserOrganVo implements Serializable {
    private String nrenName;
    private String nodeId;
    private String ifId;
}