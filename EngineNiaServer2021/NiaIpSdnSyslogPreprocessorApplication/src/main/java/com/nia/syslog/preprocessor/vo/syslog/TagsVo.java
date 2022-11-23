package com.nia.syslog.preprocessor.vo.syslog;

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
public class TagsVo implements Serializable {
    @JsonProperty("app_name")
    private String appName;
    private String facility;
    private String host;
    @JsonProperty("hostname")
    private String hostName;
    private String severity;
    private String source;
    private Timestamp timestamp;
}
