package com.nia.ip.sdn.linkage.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class LinkVo implements Serializable {
    private int id;
    private int sifId;
    private int rifId;
    private int ospfMetric;
    private int usaged;
}
