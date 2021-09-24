package com.nia.alarm.ip.preprocessor.vo.alarm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.alarm.ip.preprocessor.common.UtlDateHelper;
import com.nia.alarm.ip.preprocessor.vo.topology.TmpTopologyVo;
import com.nia.alarm.ip.preprocessor.vo.topology.TopologyObject;
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


	private Timestamp cleartime;
	private String port;
	private String slot;
	private String ptpName;
	private TopologyObject topologyObject;
	private TmpTopologyVo tmpTopologyVo;

}
