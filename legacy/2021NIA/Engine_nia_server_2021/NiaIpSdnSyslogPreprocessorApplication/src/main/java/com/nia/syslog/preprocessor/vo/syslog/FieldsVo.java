package com.nia.syslog.preprocessor.vo.syslog;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.syslog.preprocessor.common.LoggerPrint;
import com.nia.syslog.preprocessor.common.UtlDateHelper;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private long timestamp;



    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;

    }
}
