package com.nia.engine.vo.selfProcess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class MailAttVo implements Serializable {

    private float fltbpsin;
    @JsonProperty("fltbpsin_predict")
    private float fltbpsinPredict;
    @JsonProperty("fltbpsin_threshold_upper")
    private float fltbpsinThresholdUpper;
    @JsonProperty("fltbpsin_threshold_lower")
    private float fltbpsinThresholdLower;
    @JsonProperty("fltbpsin_anomaly")
    private boolean fltbpsinAnomaly;
    private float fltbpsout;
    @JsonProperty("fltbpsout_predict")
    private float fltbpsoutPredict;
    @JsonProperty("fltbpsout_threshold_upper")
    private float fltbpsoutThresholdUpper;
    @JsonProperty("fltbpsout_threshold_lower")
    private float fltbpsoutThresholdLower;
    @JsonProperty("fltbpsout_anomaly")
    private boolean fltbpsoutAnomaly;


}

