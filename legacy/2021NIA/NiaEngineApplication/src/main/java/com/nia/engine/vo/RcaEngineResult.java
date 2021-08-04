package com.nia.engine.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
@Data
@ToString
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RcaEngineResult implements Serializable{
	private static final long serialVersionUID = 4311413557236612270L;

	@JsonProperty("ticketId")
	private String ticketId;
	@JsonProperty("eventType")
	private String eventType;
	@JsonProperty("ticketType")
	private String ticketType;
//	@JsonProperty("value")
//	private String value;
//	@JsonProperty("isResult")
//	private boolean isResult;
	private String result;
	private Map<String, String> properties;
}