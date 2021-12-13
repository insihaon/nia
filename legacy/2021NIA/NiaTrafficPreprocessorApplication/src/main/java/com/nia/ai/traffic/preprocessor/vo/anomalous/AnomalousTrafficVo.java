package com.nia.ai.traffic.preprocessor.vo.anomalous;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@ToString
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnomalousTrafficVo implements Serializable {

    private String strifid;
    private String inttimestamp;
    private int fltbpsin;
    private String fltbpsin_predict;
    private String fltbpsin_threshold_upper;
    private String fltbpsin_threshold_lower;
    private boolean fltbpsin_anomaly;
    private int fltbpsout;
    private String fltbpsout_predict;
    private String fltbpsout_threshold_upper;
    private String fltbpsout_threshold_lower;
    private boolean fltbpsout_anomaly;
}
