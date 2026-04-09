package com.nia.ip.sdn.linkage.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@Data
public class LinkTrafficVo implements Serializable {
    private int id;
    private int ifId;
    private int txBitRate;
    private int rxBitRate;
    private int txPktRate;
    private int rxPktRate;
    private Timestamp measuredDateTime;
}
