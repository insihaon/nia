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
public class FieldsVo implements Serializable {
    @JsonProperty("facility_code")
    private int facilityCode;
    private String message;
    private String procid;
    @JsonProperty("severity_code")
    private int severityCode;
    private Timestamp timestamp;
}
