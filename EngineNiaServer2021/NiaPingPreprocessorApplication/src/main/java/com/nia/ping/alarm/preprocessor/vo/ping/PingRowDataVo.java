package com.nia.ping.alarm.preprocessor.vo.ping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PingRowDataVo implements Serializable {

    @JsonProperty("fields")
    private PingRowFiledsVo pingRowFiledsVo;
    @JsonProperty("tags")
    private PingRowTagsVo pingRowTagsVo;
    @JsonProperty("timestamp")
    private long collectDatetime;


}
