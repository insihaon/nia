package com.nia.alarm.ip.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class AlarmTypeVo implements Serializable{

	private String equipType;
	private String alarmType;
	private String alarmmsg;
	private String unit;
	private String sigtype;

}
