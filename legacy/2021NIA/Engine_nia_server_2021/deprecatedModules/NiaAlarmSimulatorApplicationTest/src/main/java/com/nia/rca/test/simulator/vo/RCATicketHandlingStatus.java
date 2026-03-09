package com.nia.rca.test.simulator.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class RCATicketHandlingStatus implements Serializable {
	private static final long serialVersionUID = 3038373490385067976L;

	@JsonProperty("TICKET_ID")
	private String ticketId;
	@JsonProperty("TICKET_TYPE")
	private String ticketType;
	@JsonProperty("STATUS")
	private String status;
	@JsonProperty("EVENTTYPE")
	private String eventType;
	@JsonProperty("TICKET_RESULT")
	private String ticketResult;
	@JsonProperty("DETAIL")
	private String detail;
	@JsonProperty("SOP_ID")
	private String sopId;
	@JsonProperty("REQUEST_ID")
	private String requestId;
	@JsonProperty("SERVICE_ID")
	private String serviceId;
	@JsonProperty("RESULT")
	private String result;
	@JsonProperty("USER_ID")
	private String userId;
	@JsonProperty("MAIL_CONTENT")
	private String mailContent;
}
