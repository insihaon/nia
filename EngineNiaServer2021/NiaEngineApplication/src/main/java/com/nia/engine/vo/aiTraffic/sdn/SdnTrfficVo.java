package com.nia.engine.vo.aiTraffic.sdn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SdnTrfficVo implements Serializable {
     private String nodeNum;
     private String nodeNm;
     private int sdnNodeId;
     private String ifNum;
     private int sdnIfId;
     private String ifNm;
     private int txBitRate;
     private int rxBitRate;
     private int txPktRate;
     private int rxPktRate;
     private Timestamp measuredDatetime;
}
