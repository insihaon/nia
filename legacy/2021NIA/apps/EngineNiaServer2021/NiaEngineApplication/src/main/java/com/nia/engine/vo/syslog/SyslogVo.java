package com.nia.engine.vo.syslog;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyslogVo implements Serializable {

    private String collect_seq;
    private String collect_host;
    private String source;
    private String appname;
    private String hostname;
    private String facility_code;
    private String facility;
    private String severity_code;
    private String severity;
    private String syslog_message;
    private String syslog_procid;
    private String collect_timestamp;
    private String partition_key;

}
