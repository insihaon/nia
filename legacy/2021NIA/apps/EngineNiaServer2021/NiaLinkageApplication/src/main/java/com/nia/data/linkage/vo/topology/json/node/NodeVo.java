package com.nia.data.linkage.vo.topology.json.node;

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

    @JsonProperty("node-id")
    private String nodeId;
    @JsonProperty("koren-tsdn-topology:node-status")
    private String nodeStatus;
    @JsonProperty("termination-point")
    private List<TerminationPointListVo> terminationPointListVo;

    @JsonProperty("node-id")
    public String getNodeId() {
        return nodeId;
    }

    @JsonProperty("node-id")
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @JsonProperty("koren-tsdn-topology:node-status")
    public String getNodeStatus() {
        return nodeStatus;
    }

    @JsonProperty("koren-tsdn-topology:node-status")
    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    @JsonProperty("termination-point")
    public List<TerminationPointListVo> getTerminationPointListVo() {
        return terminationPointListVo;
    }

    @JsonProperty("termination-point")
    public void setTerminationPointListVo(List<TerminationPointListVo> terminationPointListVo) {
        this.terminationPointListVo = terminationPointListVo;
    }
}
