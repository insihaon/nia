package com.nia.engine.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class RcaResult implements Serializable{
	private static final long serialVersionUID = 4325332911252157171L;

	private String rcaResultCode;
	private String rcaResultTypeCode;
	private List<BasicAlarmVo> relatedAlarmList = new LinkedList<BasicAlarmVo>();
	private List<BasicAlarmVo> associatedAlarmList = new LinkedList<BasicAlarmVo>();
	private String faultDetailCode;
	private ClusterObject clusterObject;
	private String domainCode;
	private String ruleResult;
	private String clusterNo;
	private Timestamp alarmTime;
	private String nwType; // NNI or UNI or UNKNOWN

	public void addRelatedAlarm(BasicAlarmVo basicAlarmVo){
		relatedAlarmList.add(basicAlarmVo);
	}
	public List<BasicAlarmVo> getAssociatedAlarmList() {
		return associatedAlarmList;
	}
	public void setAssociatedAlarmList(List<BasicAlarmVo> associatedAlarmList) {
		this.associatedAlarmList = associatedAlarmList;
	}
	public void addAssociatedAlarmList(BasicAlarmVo basicAlarmVo){
		associatedAlarmList.add(basicAlarmVo);
	}
}