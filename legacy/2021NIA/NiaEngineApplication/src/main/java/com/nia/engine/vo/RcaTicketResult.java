package com.nia.engine.vo;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Data
@ToString
@Scope(value = "prototype")
public class RcaTicketResult implements Serializable{
	private static final long serialVersionUID = 4311413557236612270L;

	@JsonProperty("ticketId")
	private String ticketId;
	@JsonProperty("eventType")
	private String eventType;
	@JsonProperty("value")
	private String value;
	@JsonProperty("isResult")
	private boolean isResult;
	@JsonProperty("ttciResult")
	private String ttciResult;
	@JsonProperty("setting")
	private String setting;
	private Map<String, String> properties;
}