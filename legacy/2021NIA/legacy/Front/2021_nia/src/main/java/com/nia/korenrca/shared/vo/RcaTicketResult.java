package com.nia.korenrca.shared.vo;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//@formatter:off
//@JsonInclude(JsonInclude.Include.NON_NULL) @JsonPropertyOrder({"ticketId", "eventType", "value", "properties"})
public class RcaTicketResult implements Serializable {
    @JsonProperty("ticketId") private String ticketId;
    @JsonProperty("eventType") private String eventType;
    @JsonProperty("value") private String value;
    @JsonProperty("properties") private Map<String, Object> properties;
    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public RcaTicketResult() {
    }

    @JsonProperty("ticketId")
    public String getTicketId() {
        return ticketId;
    }

    @JsonProperty("ticketId")
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("properties")
    public Map<String,Object> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(Map<String,Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
//@formatter:on
