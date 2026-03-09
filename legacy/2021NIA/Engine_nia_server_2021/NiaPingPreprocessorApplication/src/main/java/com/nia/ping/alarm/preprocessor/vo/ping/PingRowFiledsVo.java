package com.nia.ping.alarm.preprocessor.vo.ping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PingRowFiledsVo implements Serializable {

    @JsonProperty("packets_received")
    private String packetsReceived;
    @JsonProperty("percent_packet_loss")
    private int percentPacketLoss;
    @JsonProperty("result_code")
    private String resultCode;
    @JsonProperty("packets_transmitted")
    private String packetsTransmitted;
}
