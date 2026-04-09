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
public class MailEquipAndPortVo implements Serializable {

    @JsonProperty("ticket_id")
    private String ticketId;
    @JsonProperty("root_cause_sysnamea")
    private String rootCauseSysnamea;
    @JsonProperty("root_cause_sysnamez")
    private String rootCauseSysnamez;
    @JsonProperty("root_cause_unita")
    private String rootCauseUnita;
    @JsonProperty("root_cause_unitz")
    private String rootCauseUnitz;
    @JsonProperty("root_cause_alarm_noa")
    private String rootCauseAlarmNoa;
    @JsonProperty("root_cause_alarm_noz")
    private String rootCauseAlarmNoz;
    @JsonProperty("root_cause_porta")
    private String rootCausePorta;
    @JsonProperty("root_cause_portz")
    private String rootCausePortz;
}
