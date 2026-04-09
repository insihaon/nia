package com.nia.data.linkage.vo.topology.json.link;

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
public class LinkSourceVo implements Serializable {

    @JsonProperty("source-tp")
    private String sourceTp;
    @JsonProperty("source-node")
    private String sourceNode;

    @JsonProperty("source-tp")
    public String getSourceTp() {
        return sourceTp;
    }

    @JsonProperty("source-tp")
    public void setSourceTp(String sourceTp) {
        this.sourceTp = sourceTp;
    }

    @JsonProperty("source-node")
    public String getSourceNode() {
        return sourceNode;
    }

    @JsonProperty("source-node")
    public void setSourceNode(String sourceNode) {
        this.sourceNode = sourceNode;
    }
}
