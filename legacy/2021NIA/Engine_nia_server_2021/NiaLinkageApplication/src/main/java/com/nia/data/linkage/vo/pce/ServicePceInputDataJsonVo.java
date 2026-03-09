package com.nia.data.linkage.vo.pce;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class ServicePceInputDataJsonVo implements Serializable {
    @JsonProperty("dst-tp-id")
    private String dstTpId;
    @JsonProperty("topology-id")
    private String topologyId;
    @JsonProperty("count")
    private String count;
    @JsonProperty("pir")
    private String pir;
    @JsonProperty("dst-node-id")
    private String dstNodeId;
    @JsonProperty("cir")
    private String cir;
    @JsonProperty("src-node-id")
    private String srcNodeId;
    @JsonProperty("src-tp-id")
    private String srcTpId;

    @JsonProperty("dst-tp-id")
    public String getDstTpId() {
        return dstTpId;
    }

    @JsonProperty("dst-tp-id")
    public void setDstTpId(String dstTpId) {
        this.dstTpId = dstTpId;
    }

    @JsonProperty("topology-id")
    public String getTopologyId() {
        return topologyId;
    }

    @JsonProperty("topology-id")
    public void setTopologyId(String topologyId) {
        this.topologyId = topologyId;
    }

    @JsonProperty("count")
    public String getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(String count) {
        this.count = count;
    }

    @JsonProperty("pir")
    public String getPir() {
        return pir;
    }

    @JsonProperty("pir")
    public void setPir(String pir) {
        this.pir = pir;
    }

    @JsonProperty("dst-node-id")
    public String getDstNodeId() {
        return dstNodeId;
    }

    @JsonProperty("dst-node-id")
    public void setDstNodeId(String dstNodeId) {
        this.dstNodeId = dstNodeId;
    }

    @JsonProperty("cir")
    public String getCir() {
        return cir;
    }

    @JsonProperty("cir")
    public void setCir(String cir) {
        this.cir = cir;
    }

    @JsonProperty("src-node-id")
    public String getSrcNodeId() {
        return srcNodeId;
    }

    @JsonProperty("src-node-id")
    public void setSrcNodeId(String srcNodeId) {
        this.srcNodeId = srcNodeId;
    }

    @JsonProperty("src-tp-id")
    public String getSrcTpId() {
        return srcTpId;
    }

    @JsonProperty("src-tp-id")
    public void setSrcTpId(String srcTpId) {
        this.srcTpId = srcTpId;
    }
}
