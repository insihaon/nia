package com.nia.ping.alarm.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@Data
public class BasicAlarmVo implements Serializable {

	private String alarmno;
	private String sysname;
	private String equipCode;
	private String equipName;
	private String equiptype;
	private String unit;
	private Timestamp alarmtime;
	private Timestamp receivetime;
	private String alarmloc;
	private String alarmmsg;
	private String alarmmsgOriginal;
	private String alarmCode;
	private String alarmlevel;
	private String alarmType;
	private String domain;
	private String ifNum;
	private String ifId;
	private String ipAddr;
	private Timestamp cleartime;

}
