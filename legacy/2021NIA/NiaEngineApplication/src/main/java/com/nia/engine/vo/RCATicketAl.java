package com.nia.engine.vo;

import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrfficVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficListVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficVo;
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
	private AnomalousTrafficVo rootCauseAnomalousTrafficAlarmInfoA;
	private AnomalousTrafficVo rootCauseAnomalousTrafficAlarmInfoZ;
	private NoxiousTrfficVo rootCauseNoxiousTrfficAlarmInfoA;
	private NoxiousTrfficVo rootCauseNoxiousTrfficAlarmInfoZ;

	private String rootCausePtpnameA;
	private String rootCausePtpnameZ;

	private boolean cur = false; // 과거 or 현재 티켓 구분 플래그
}
