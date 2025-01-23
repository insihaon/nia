package com.nia.syslog.preprocessor.vo.selfProcess;

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
public class SyslogSopVo implements Serializable {
    @JsonProperty("sop_id")
    private String sopId;
    private String alarmno ;
    @JsonProperty("fault_classify")
    private String faultClassify;
    @JsonProperty("fault_type")
    private String faultType;
    @JsonProperty("fault_detail_content")
    private String faultDetailContent;
    @JsonProperty("etc_content")
    private String etcContent;
    private String status  ;
    @JsonProperty("proc_status")
    private String procStatus;
    @JsonProperty("alarm_occur_time")
    private String alarmOccurTime;
    @JsonProperty("handling_fin_user")
    private String handlingFinUser;
    @JsonProperty("handling_fin_time")
    private String handlingFinTime      ;
    private String alarmloc;

}
