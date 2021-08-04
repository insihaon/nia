package com.nia.data.linkage.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class AlarmVo implements Serializable {
    private String elkAlarmId;
    private String alarmno;
    private String sysname;
    private String equiptype;
    private String unit;
    private Timestamp alarmtime;
    private Timestamp receivetime;
    private String alarmloc;
    private String alarmmsg;
    private String alarmlevel;
    private String description;
}
