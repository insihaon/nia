package com.nia.ems.linkage.vo.alarm;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
@Component()
@Scope(value = "prototype")
public class AlarmVo implements Serializable {
    private String alarmno;
    private String sysname;
    private String equiptype;
    private String unit;
    private Timestamp alarmtime;
    private Timestamp receivetime;
    private String alarmloc;
    private String alarmmsg;
    private String alarmlevel;
    private String lineName;
}
