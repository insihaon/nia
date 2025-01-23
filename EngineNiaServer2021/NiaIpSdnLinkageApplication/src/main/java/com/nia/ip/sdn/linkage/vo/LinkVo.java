package com.nia.ip.sdn.linkage.vo;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
@Getter
public class LinkVo implements Serializable {
    private int id;
    private int sifId;
    private int rifId;
    private int vsifId;
    private String speed;
    private int ospfMetric;

    public int getLatencyStd() {
        return latencyStd;
    }

    private int latencyStd;
    private int activeStandby;
    private int usaged;

}
