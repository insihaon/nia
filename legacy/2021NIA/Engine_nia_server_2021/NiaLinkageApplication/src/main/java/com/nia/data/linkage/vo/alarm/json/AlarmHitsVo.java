package com.nia.data.linkage.vo.alarm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Scope(value = "prototype")
public class AlarmHitsVo implements Serializable {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("_source")
    private AlarmDataVo alarmVo;

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("_source")
    public AlarmDataVo getAlarmVo() {
        return alarmVo;
    }

    @JsonProperty("_source")
    public void setAlarmVo(AlarmDataVo alarmVo) {
        this.alarmVo = alarmVo;
    }
}
