package com.nia.tsdn.service.result.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class NodeVo implements Serializable {

    @JsonProperty("topology-id")
    private String topologyId;
    @JsonProperty("node-id")
    private String nodeId;
    @JsonProperty("is-root-node")
    private String isRootNode;

    @JsonProperty("topology-id")
    public String getTopologyId() {
        return topologyId;
    }

    @JsonProperty("topology-id")
    public void setTopologyId(String topologyId) {
        this.topologyId = topologyId;
    }

    @JsonProperty("node-id")
    public String getNodeId() {
        return nodeId;
    }

    @JsonProperty("node-id")
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @JsonProperty("is-root-node")
    public String getIsRootNode() {
        return isRootNode;
    }

    @JsonProperty("is-root-node")
    public void setIsRootNode(String isRootNode) {
        this.isRootNode = isRootNode;
    }
}
