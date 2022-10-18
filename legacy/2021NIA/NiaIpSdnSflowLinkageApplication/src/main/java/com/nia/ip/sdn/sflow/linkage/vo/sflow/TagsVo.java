package com.nia.ip.sdn.sflow.linkage.vo.sflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Scope(value = "prototype")
public class TagsVo implements Serializable {
    @JsonProperty("agent_address")
    private String agentAddress;
    @JsonProperty("dst_ip")
    private String dstIp;
    @JsonProperty("dst_mac")
    private String dstMac;
    @JsonProperty("dst_port")
    private String dstPort;
    @JsonProperty("ether_type")
    private String etherType;
    @JsonProperty("header_protocol")
    private String headerProtocol;
    private String host;
    @JsonProperty("input_ifindex")
    private String inputIfindex;
    @JsonProperty("output_ifindex")
    private String outputIfindex;
    @JsonProperty("sample_direction")
    private String sampleDirection;
    @JsonProperty("source_id_index")
    private String sourceIdIndex;
    @JsonProperty("source_id_type")
    private String sourceIdType;
    @JsonProperty("src_ip")
    private String srcIp;
    @JsonProperty("src_mac")
    private String srcMac;
    @JsonProperty("src_port")
    private String srcPort;
}
