package com.nia.syslog.preprocessor.vo.rule;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.*;
import java.sql.Timestamp;
import java.io.Serializable;

@Data
@Component
@Scope(value = "prototype")
public class SyslogRuleVo implements Serializable {

	private int syslogRuleId;
	private String syslogRuleNm;
	private String syslogContent;
	private String nodeType;
	private int priorityOrder;
	private String alarmSeverity;
	private String occurStr1;
	private String occurStr2;
	private String occurStr3;
	private String occurExceptStr1;
	private String occurExceptStr2;
	private String occurExceptStr3;
	private String useYn;
}
