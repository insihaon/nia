package com.nia.engine.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class RCATicketHandlingStatus implements Serializable {

	@JsonProperty("ticket_id")
	private String ticketId;
	@JsonProperty("ticket_type")
	private String ticketType;
	@JsonProperty("status")
	private String status;
	@JsonProperty("eventType")
	private String eventType;
	@JsonProperty("ticket_result")
	private String ticketResult;
	@JsonProperty("detail")
	private String detail;
	@JsonProperty("sop_id")
	private String sopId;
	@JsonProperty("request_id")
	private String requestId;
	@JsonProperty("service_id")
	private String serviceId;
	@JsonProperty("result")
	private String result;
	@JsonProperty("user_ids")
	private String userIds;
	@JsonProperty("mail_content")
	private String mailContent;
//	@JsonProperty("startTime")
//	private Timestamp startTime;
//	@JsonProperty("endTime")
//	private Timestamp endTime;
	@JsonProperty("fault_classify")
	private String faultClassify;
	@JsonProperty("fault_type")
	private String faultType;
	@JsonProperty("fault_detail_content")
	private String faultDetailContent;
	@JsonProperty("etc_content")
	private String etcContent;
	@JsonProperty("ai_accuracy")
	private String aiAccuracy;
	@JsonProperty("fault_type_content")
	private String faultTypeContent;
	@JsonProperty("start_time")
	private Timestamp startTime;
	@JsonProperty("end_time")
	private Timestamp endTime;

	private String eventno;
	@JsonProperty("title")
	private String title;
}
