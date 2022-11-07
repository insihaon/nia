package com.nia.ip.sdn.syslog.linkage.vo.syslog;

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
    private String appname;
    private String facility;
    private String host;
    private String hostname;
    private String severity;
    private String source;
}
