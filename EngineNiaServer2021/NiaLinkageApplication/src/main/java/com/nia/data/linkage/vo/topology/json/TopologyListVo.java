package com.nia.data.linkage.vo.topology.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.data.linkage.vo.topology.json.link.LinkVo;
import com.nia.data.linkage.vo.topology.json.node.NodeVo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class TopologyListVo implements Serializable {

    @JsonProperty("topology-id")
    private String topologyId;
    @JsonProperty("link")
    private List<LinkVo> linkVo;
    @JsonProperty("node")
    private List<NodeVo> nodeVo;

    @JsonProperty("topology-id")
    public String getTopologyId() {
        return topologyId;
    }

    @JsonProperty("topology-id")
    public void setTopologyId(String topologyId) {
        this.topologyId = topologyId;
    }

    @JsonProperty("node")
    public List<NodeVo> getNodeVo() {
        return nodeVo;
    }

    @JsonProperty("node")
    public void setNodeVo(List<NodeVo> nodeVo) {
        this.nodeVo = nodeVo;
    }

    @JsonProperty("link")
    public List<LinkVo> getLinkVo() {
        return linkVo;
    }

    @JsonProperty("link")
    public void setLinkVo(List<LinkVo> linkVo) {
        this.linkVo = linkVo;
    }
}
