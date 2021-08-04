package com.nia.engine.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class RCATicketAl implements Serializable {
	private static final long serialVersionUID = -6843169573897486305L;
	private String ticketId;
	private String ticketAlId;
	private String rootCauseAlarmNoA;
	private String rootCauseSysnameA;
	private String rootCauseEquipTypeA;
	private String rootCauseUnitA;
	private String rootCauseSlotA;
	private String rootCausePortA;
	private String rootCauseAlarmLevelA;
	private String rootCauseAlarmNoZ;
	private String rootCauseSysnameZ;
	private String rootCauseEquipTypeZ;
	private String rootCauseUnitZ;
	private String rootCauseSlotZ;
	private String rootCausePortZ;
	private String rootCauseAlarmLevelZ;
	private Timestamp insert_time;
	private BasicAlarmVo rootCauseAlarmInfoA;
	private BasicAlarmVo rootCauseAlarmInfoZ;

	private String rootCausePtpnameA;
	private String rootCausePtpnameZ;
}
