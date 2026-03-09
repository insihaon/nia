package com.nia.syslog.preprocessor.vo.selfProcess;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyslogAlarmVo implements Serializable {

    private String alarmno;
    private Timestamp alarmtime;
    @JsonProperty("node_num")
    private String nodeNum;
    @JsonProperty("node_nm")
    private String nodeNm;
    private String alarmmsg;
    private String alarmlvl;
    private String etc;
    @JsonProperty("rule_id")
    private int ruleId;
    private String alarmloc;
    private String status;

}
