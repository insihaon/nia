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
    private int alarmno;
    private Timestamp alarmtime;
    private int nodeNum;
    private String nodeNm;
    private String alarmmsg;
    private int alarmlvl;
    private String etc;
    private int ruleId;

    @JsonProperty("AlarmNo")
    public int getAlarmno() {
        return alarmno;
    }

    @JsonProperty("AlarmTime")
    public Timestamp getAlarmtime() {
        return alarmtime;
    }

    @JsonProperty("NodeNum")
    public int getNodeNum() {
        return nodeNum;
    }

    @JsonProperty("NodeNm")
    public String getNodeNm() {
        return nodeNm;
    }

    @JsonProperty("AlarmMsg")
    public String getAlarmmsg() {
        return alarmmsg;
    }

    @JsonProperty("AlarmLvl")
    public int getAlarmlvl() {
        return alarmlvl;
    }

    @JsonProperty("Etc")
    public String getEtc() {
        return etc;
    }

    @JsonProperty("RuleId")
    public int getRuleId() {
        return ruleId;
    }
}
