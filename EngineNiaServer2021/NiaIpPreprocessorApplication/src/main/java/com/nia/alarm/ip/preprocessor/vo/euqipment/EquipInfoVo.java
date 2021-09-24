package com.nia.alarm.ip.preprocessor.vo.euqipment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class EquipInfoVo implements Serializable{

	private String tid;
	private String sysname;
	private String model;
}