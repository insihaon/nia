package com.nia.alarm.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class AlNormalizerVo implements Serializable{

	private String manufacturer;
	private String equipType;
	private String nameCode;
	private String alarmMsg;
	private String normalizedAlarm;
	private String unit;
	private String alarmType;
}