package com.nia.engine.vo.aiTraffic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EngineNttTrafficResultVo {
    private String gb;
    private String ticketId;
    private String jsonKey;
    private String traffic_type;
    private String normalTrafficProbability;
    private String tcpSynFloodingProbability;
    private String landAttackProbability;
    private String pingOfDeathProbability;
    private String udpFloodingProbability;
}
