package com.nia.engine.vo.sdn.traffic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@ToString
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SdnTrafficInfoVo implements Serializable {
    private String strifid;
    private String strifnm;
    private String strresid;
    private String strresnm;
    private Timestamp measuredDatetime;
    private float fltbpsin;
    private float fltbpsout;
    private float fltppsin;
    private float fltppsout;
    private Timestamp insertTime;

}
