package com.nia.data.linkage.vo.topology.json.service;

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
public class ServiceNodeDataVo implements Serializable {

    @JsonProperty("node-index")
    private int nodeIndex;
    @JsonProperty("is-root-node")
    private Boolean isRootNode;
    @JsonProperty("node-id")
    private String nodeId;
    @JsonProperty("tag-list")
    private List<ServiceNodeTagDataVo> serviceNodeTagDataVoList;
    @JsonProperty("tp-id")
    private String tpId;

    @JsonProperty("node-index")
    public int getNodeIndex() {
        return nodeIndex;
    }

    @JsonProperty("node-index")
    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    @JsonProperty("is-root-node")
    public Boolean getRootNode() {
        return isRootNode;
    }

    @JsonProperty("is-root-node")
    public void setRootNode(Boolean rootNode) {
        isRootNode = rootNode;
    }

    @JsonProperty("node-id")
    public String getNodeId() {
        return nodeId;
    }

    @JsonProperty("node-id")
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @JsonProperty("tag-list")
    public List<ServiceNodeTagDataVo> getServiceNodeTagDataVoList() {
        return serviceNodeTagDataVoList;
    }

    @JsonProperty("tag-list")
    public void setServiceNodeTagDataVoList(List<ServiceNodeTagDataVo> serviceNodeTagDataVoList) {
        this.serviceNodeTagDataVoList = serviceNodeTagDataVoList;
    }

    @JsonProperty("tp-id")
    public String getTpId() {
        return tpId;
    }

    @JsonProperty("tp-id")
    public void setTpId(String tpId) {
        this.tpId = tpId;
    }
}
