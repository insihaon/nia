package com.nia.alarm.ip.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
public class ClearAlarmVo {
    private String alarmno;
    private Timestamp clearTime;
    private String domain;
}
