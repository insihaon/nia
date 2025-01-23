package com.nia.engine.vo.sdn.traffic;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SdnTrafficVo implements Serializable {
    private String traffic_gb;
    private String sdn_node_id;
    private String strresid;
    private String strifid;
    private String strresnm;
    @JsonProperty("inttimestamp")
    private long measured_datetime;
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
    private String profile_title;
    private Timestamp insert_time;
    @JsonProperty("nodeId")
    private String node_id;

}
