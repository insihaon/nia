package com.nia.engine.vo.sdn.factor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeFactorVo implements Serializable {
    @JsonProperty("sdn_node_id")
    private String id;
    private long measured_datetime;
    private String predicted_time;
    @JsonProperty("cpu_predicted")
    private float cpu_usage;
    @JsonProperty("mem_predicted")
    private float mem_usage;
    private String node_nm;
    private String node_num;
}
