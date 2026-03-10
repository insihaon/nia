package com.nia.data.linkage.vo.topology.json.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class ServicePceLinkDataVo implements Serializable {

    @JsonProperty("tunnel-id")
    private String tunnelId;
    @JsonProperty("dst-node-id")
    private String dstNodeId;
    @JsonProperty("src-tp-id")
    private String srcTpId;
    @JsonProperty("dst-tp-id")
    private String dstTpId;
    @JsonProperty("src-node-id")
    private String srcNodeId;

    @JsonProperty("tunnel-id")
    public String getTunnelId() {
        return tunnelId;
    }

    @JsonProperty("tunnel-id")
    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    @JsonProperty("dst-node-id")
    public String getDstNodeId() {
        return dstNodeId;
    }

    @JsonProperty("dst-node-id")
    public void setDstNodeId(String dstNodeId) {
        this.dstNodeId = dstNodeId;
    }

    @JsonProperty("src-tp-id")
    public String getSrcTpId() {
        return srcTpId;
    }

    @JsonProperty("src-tp-id")
    public void setSrcTpId(String srcTpId) {
        this.srcTpId = srcTpId;
    }

    @JsonProperty("dst-tp-id")
    public String getDstTpId() {
        return dstTpId;
    }

    @JsonProperty("dst-tp-id")
    public void setDstTpId(String dstTpId) {
        this.dstTpId = dstTpId;
    }

    @JsonProperty("src-node-id")
    public String getSrcNodeId() {
        return srcNodeId;
    }

    @JsonProperty("src-node-id")
    public void setSrcNodeId(String srcNodeId) {
        this.srcNodeId = srcNodeId;
    }
}
