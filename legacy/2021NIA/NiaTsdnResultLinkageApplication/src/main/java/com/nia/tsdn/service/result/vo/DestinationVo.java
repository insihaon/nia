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
public class DestinationVo implements Serializable {

    @JsonProperty("topology-id")
    private String topologyId;
    @JsonProperty("dst-node-id")
    private String dstNodeId;
    @JsonProperty("dst-tp-id")
    private String dstTpId;
    @JsonProperty("tag-list")
    private List<TagVo> tagList;

    @JsonProperty("topology-id")
    public String getTopologyId() {
        return topologyId;
    }

    @JsonProperty("topology-id")
    public void setTopologyId(String topologyId) {
        this.topologyId = topologyId;
    }

    @JsonProperty("dst-node-id")
    public String getDstNodeId() {
        return dstNodeId;
    }

    @JsonProperty("dst-node-id")
    public void setDstNodeId(String dstNodeId) {
        this.dstNodeId = dstNodeId;
    }

    @JsonProperty("dst-tp-id")
    public String getDstTpId() {
        return dstTpId;
    }

    @JsonProperty("dst-tp-id")
    public void setDstTpId(String dstTpId) {
        this.dstTpId = dstTpId;
    }

    @JsonProperty("tag-list")
    public List<TagVo> getTagList() {
        return tagList;
    }

    @JsonProperty("tag-list")
    public void setTagList(List<TagVo> tagList) {
        this.tagList = tagList;
    }
}
