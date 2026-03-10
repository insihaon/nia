package com.nia.alarm.preprocessor.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class TopologyNeVo implements Serializable{

	private String sysname;
	private String ptpNameBau;
	private String ptpNamePau;
}