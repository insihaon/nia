package com.nia.alarm.ip.preprocessor.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class UniNeObject implements Serializable{
	private String sysname;
	private String ptpname;
}