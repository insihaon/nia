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
public class AlarmHitsDataVo implements Serializable {

    @JsonProperty("hits")
    private AlarmHitsHitsDataVo alarmHitsHitsDataVo;

    @JsonProperty("hits")
    public AlarmHitsHitsDataVo getAlarmHitsHitsDataVo() {
        return alarmHitsHitsDataVo;
    }

    @JsonProperty("hits")
    public void setAlarmHitsHitsDataVo(AlarmHitsHitsDataVo alarmHitsHitsDataVo) {
        this.alarmHitsHitsDataVo = alarmHitsHitsDataVo;
    }
}
