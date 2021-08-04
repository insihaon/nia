package com.nia.data.linkage.vo.alarm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Scope(value = "prototype")
public class AlarmHitsHitsDataVo implements Serializable {

    @JsonProperty("hits")
    private List<AlarmHitsVo> alarmHitsVo;

    @JsonProperty("hits")
    public List<AlarmHitsVo> getAlarmHitsVo() {
        return alarmHitsVo;
    }

    @JsonProperty("hits")
    public void setAlarmHitsVo(List<AlarmHitsVo> alarmHitsVo) {
        this.alarmHitsVo = alarmHitsVo;
    }
}
