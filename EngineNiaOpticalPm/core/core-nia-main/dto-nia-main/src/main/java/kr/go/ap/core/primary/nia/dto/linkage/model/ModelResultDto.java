package kr.go.ap.core.primary.nia.dto.linkage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelResultDto implements Serializable {
    @JsonProperty("trunk_id")
    private String trunkId;
    @JsonProperty("trunk_name")
    private String trunkName;
    @JsonProperty("tid")
    private String tid;
    @JsonProperty("sysname")
    private String sysname;
    @JsonProperty("roadm_code")
    private String roadmCode;
    @JsonProperty("ptpname")
    private String ptpname;
    @JsonProperty("port")
    private String port;
    @JsonProperty("ocrtime")
    private Timestamp ocrtime;
    @JsonProperty("in_out")
    private String inOut;
    @JsonProperty("value_cur")
    private float valueCur;
    @JsonProperty("value_max")
    private float valueMax;
    @JsonProperty("value_min")
    private float valueMin;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("routenum")
    private int routeNum;
    @JsonProperty("lowSignal")
    private boolean isLowSignal;
}
