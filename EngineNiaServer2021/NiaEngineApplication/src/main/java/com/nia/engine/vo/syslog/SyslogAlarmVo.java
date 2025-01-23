package com.nia.engine.vo.syslog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class SyslogAlarmVo implements Serializable {

    private String alarmno;
    private Timestamp alarmtime;
    private String node_num;
    private String node_nm;
    private String alarmmsg;
    private String alarmlvl;
    private String etc;
    private int rule_id;
    private String alarmloc;
    private String status;

}
