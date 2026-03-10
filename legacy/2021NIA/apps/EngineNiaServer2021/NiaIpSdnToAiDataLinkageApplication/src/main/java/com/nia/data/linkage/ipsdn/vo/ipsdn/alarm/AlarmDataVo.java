package com.nia.data.linkage.ipsdn.vo.ipsdn.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Component()
@Scope(value = "prototype")
public class AlarmDataVo implements Serializable {
    private String alarmno;
    private Timestamp alarmtime;
    private String nodeNum;
    private String nodeNm;
    private String alarmmsg;
    private int alarmlvl;
    private String etc;
    private int ruleId;

    @JsonProperty("alarmno")
    public String getAlarmno() {
        return alarmno;
    }

    @JsonProperty("alarm_time")
    public Timestamp getAlarmtime() {
        return alarmtime;
    }

    @JsonProperty("node_num")
    public String getNodeNum() {
        return nodeNum;
    }

    @JsonProperty("node_nm")
    public String getNodeNm() {
        return nodeNm;
    }

    @JsonProperty("alarmmsg")
    public String getAlarmmsg() {
        return alarmmsg;
    }

    @JsonProperty("alarmlvl")
    public int getAlarmlvl() {
        return alarmlvl;
    }

    @JsonProperty("etc")
    public String getEtc() {
        return etc;
    }

    @JsonProperty("rule_id")
    public int getRuleId() {
        return ruleId;
    }
}
