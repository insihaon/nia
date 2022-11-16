package com.nia.syslog.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
public class SysLogAlarmVo implements Serializable {

	private String alarmno;
    private Timestamp alarmtime;
	private String nodeNum;
	private String nodeNm;
    private String alarmmsg;
	private String alarmlvl;
    private String etc;
	private int ruleId;
}
