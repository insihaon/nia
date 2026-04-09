package kr.go.ap.core.primary.nia.dto.rca.engine.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RcaEngineResultDto implements Serializable {

    @JsonProperty("ticketId")
    private String ticketId;
    @JsonProperty("eventType")
    private String eventType;
    @JsonProperty("ticketType")
    private String ticketType;
    private String result;
    private Map<String, String> properties;

}