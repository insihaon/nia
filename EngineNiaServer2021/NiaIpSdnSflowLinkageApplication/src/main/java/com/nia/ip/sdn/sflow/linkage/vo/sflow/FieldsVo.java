package com.nia.ip.sdn.sflow.linkage.vo.sflow;

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
public class FieldsVo implements Serializable {
    private int bytes;
    private int drops;
    @JsonProperty("frame_length")
    private int frameLength;
    @JsonProperty("header_length")
    private int headerLength;
    @JsonProperty("ip_dscp")
    private String ipDscp;
    @JsonProperty("ip_ecn")
    private String ipEcn;
    @JsonProperty("ip_flags")
    private int ipFlags;
    @JsonProperty("ip_fragment_offset")
    private int ipFragmentOffset;
    @JsonProperty("ip_total_length")
    private int ipTotalLength;
    @JsonProperty("ip_ttl")
    private int ipTtl;
    @JsonProperty("sampling_rate")
    private int samplingRate;
    @JsonProperty("tcp_header_length")
    private int tcpHeaderLength;
    @JsonProperty("tcp_urgent_pointer")
    private int tcpUrgentPointer;
    @JsonProperty("tcp_window_size")
    private int tcpWindowSize;
}
