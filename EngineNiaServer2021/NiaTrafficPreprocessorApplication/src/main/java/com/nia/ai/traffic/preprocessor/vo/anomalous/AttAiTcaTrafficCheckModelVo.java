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
public class AttAiTcaTrafficCheckModelVo implements Serializable {
    private String nodeNum;
    private String nodeId;
    private String nodeNm;
    private String ifNum;
    private String ifId;
    private String ifNm;
    private String tcaAlertyn;
    private String tcaAlertDirection;
    private Integer modelId;
    private Integer ipsdnIfId;
    private Integer inThresholdValue;
    private Integer outThresholdValue;
    private String lastCheckTime;
}
