package com.nia.ai.traffic.preprocessor.vo.anomalous;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficListVo implements Serializable {
    private String strresid;
    private String strresname;
    private String nodeId;
    private Integer strifid;
    private String strifnm;
    private Timestamp inttimestamp;
    private String ticketRcaResultCode;
}
