package kr.go.ap.core.primary.nia.dto.pm.optical;

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
public class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    OpticalPerformanceDto implements Serializable {
    private String trunkId;
    private String trunkName;
    private String tid;
    private String sysname;
    private String roadmCode;
    private String ptpname;
    private String port;
    private Timestamp ocrtime;
    private String inOut;
    private float valueCur;
    private float valueMax;
    private float valueMin;
    private String direction;
    private int routeNum;
    @JsonProperty("lowSignal")
    private boolean isLowSignal;


}


