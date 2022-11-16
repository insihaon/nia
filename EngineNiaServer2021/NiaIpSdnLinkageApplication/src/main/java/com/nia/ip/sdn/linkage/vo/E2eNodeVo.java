package com.nia.ip.sdn.linkage.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class E2eNodeVo implements Serializable {
    private int id;
    private int sNodeId;
    private int rNodeId;
    private int usaged;

}
