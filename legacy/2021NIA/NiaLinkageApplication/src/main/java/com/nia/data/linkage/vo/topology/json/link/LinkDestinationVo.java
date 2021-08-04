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
public class LinkDestinationVo implements Serializable {

    @JsonProperty("dest-tp")
    private String destTp;
    @JsonProperty("dest-node")
    private String destNode;

    @JsonProperty("dest-tp")
    public String getDestTp() {
        return destTp;
    }

    @JsonProperty("dest-tp")
    public void setDestTp(String destTp) {
        this.destTp = destTp;
    }

    @JsonProperty("dest-node")
    public String getDestNode() {
        return destNode;
    }

    @JsonProperty("dest-node")
    public void setDestNode(String destNode) {
        this.destNode = destNode;
    }
}
