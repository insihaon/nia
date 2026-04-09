package com.nia.engine.vo.falutEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 *
 */
@Component
@Data
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaultEventVo implements Serializable {
	@JsonProperty("eventno")
	private String eventno;
	@JsonProperty("eventGb")
	private String eventGb;
	@JsonProperty("title")
	private String title;
	@JsonProperty("startTime")
	private String startTime;
	@JsonProperty("endTime")
	private String endTime;
	@JsonProperty("alarmList")
	private List<FaultEventAlarmDataVo> faultEventAlarmList;
	@JsonProperty("performanceList")
	private List<FaultEventPerformanceDataVo> faultEventPerformanceList;
	@JsonProperty("nni_topologyList")
	private List<FaultEventNniTopologyDataVo> faultEventNniTopologyList;
	@JsonProperty("uni_topologyList")
	private List<FaultEventUniTopologyDataVo> faultEventUniTopologyList;
	@JsonProperty("cvnms_errorList")
	private List<FaultEventIpAlarmVo> faultEventCvnmsErrorList;
	@JsonProperty("cvnms_perfList")
	private List<FaultEventPerfVo> faultEventCvnmsPerfList;
	@JsonProperty("cvnms_sflowList")
	private List<FaultEventSflowLogVo> faultEventCvnmsSflowList;
	@JsonProperty("cvnms_resourceList")
	private List<FaultEventIpCvnmsResourceVo> faultEventCvnmsResourceList;
	@JsonProperty("cvnms_resource_ifList")
	private List<FaultEventIpCvnmsResourceIfVo> faultEventCvnmsResourceIfList;

}
