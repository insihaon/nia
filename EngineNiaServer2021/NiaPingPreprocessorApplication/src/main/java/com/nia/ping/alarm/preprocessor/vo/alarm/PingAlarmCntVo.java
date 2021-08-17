package com.nia.ping.alarm.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
public class PingAlarmCntVo implements Serializable {
    private String url;
    private int wrFaultCount;
    private int miFaultCount;
    private int mjFaultCount;
    private int crFaultCount;
}
