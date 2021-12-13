package com.nia.engine.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RCAHandlingStatus implements Serializable {

	@JsonProperty("TICKET_ID")
	private String ticketId;
	@JsonProperty("HANDLING_TIME")
	private Timestamp handlingTime;
	@JsonProperty("STATUS")
	private String status;
	@JsonProperty("AUTOMATION")
	private String autoMation;
	@JsonProperty("HANDLING_USER")
	private String handlingUser;
	@JsonProperty("HANDLING_DEPT")
	private String handlingDept;
	@JsonProperty("HANDLING_AGENCY")
	private String handlingAgency;
	@JsonProperty("HANDLING_CONTENT")
	private String handlingContent;
	@JsonProperty("HANDLING_FIN_CONTENT")
	private String handlingFinContent;
	@JsonProperty("RCA_ACCURACY")
	private String rcaAccuracy;
	@JsonProperty("REASON_LEVEL1")
	private String reasonLevel1;
	@JsonProperty("REASON_LEVEL2")
	private String reasonLevel2;
	@JsonProperty("REASON_LEVEL3")
	private String reasonLevel3;
	@JsonProperty("CHARGE_LEVEL1")
	private String chargeLevel1;
	@JsonProperty("CHARGE_LEVEL2")
	private String chargeLevel2;
	@JsonProperty("FIN_LEVEL1")
	private String finLevel1;
	@JsonProperty("HANDLING_FIN_USER")
	private String handlingFinUser;
	@JsonProperty("HANDLING_FIN_DEPT")
	private String handlingFinDept;
	@JsonProperty("HANDLING_FIN_AGENCY")
	private String handlingFinAgency;
	private Timestamp handlingAckTime;
	private Timestamp ticketGenerationTime;

	private String autoYn;
	private String clearYn;
	@JsonProperty("TICKET_TYPE")
	private String ticketType;
	@JsonProperty("FAULT_TYPE")
	private String faultType;
}
