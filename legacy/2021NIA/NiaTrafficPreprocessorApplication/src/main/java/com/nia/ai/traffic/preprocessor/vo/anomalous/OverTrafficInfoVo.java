package com.nia.ai.traffic.preprocessor.vo.anomalous;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OverTrafficInfoVo implements Serializable {
    private int id;
    private int ifId;
    private int txBitRate;
    private int rxBitRate;
    private int txPktRate;
    private int rxPktRate;
    private String direction;
    private String measuredDatetime;
}
