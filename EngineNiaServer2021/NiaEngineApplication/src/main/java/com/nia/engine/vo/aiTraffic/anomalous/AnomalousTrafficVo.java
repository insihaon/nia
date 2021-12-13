package com.nia.engine.vo.aiTraffic.anomalous;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnomalousTrafficVo implements Serializable {

    private String strifid;
    private String if_id;
    private String strreid;
    private String node_id;
    private int inttimestamp;
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
