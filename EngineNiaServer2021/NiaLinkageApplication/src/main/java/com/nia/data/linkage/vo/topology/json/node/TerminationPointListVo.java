package com.nia.data.linkage.vo.topology.json.node;

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
public class TerminationPointListVo implements Serializable {

    @JsonProperty("tp-id")
    private String tpId;
    @JsonProperty("koren-tsdn-topology:port-type")
    private String portType;

    @JsonProperty("tp-id")
    public String getTpId() {
        return tpId;
    }

    @JsonProperty("tp-id")
    public void setTpId(String tpId) {
        this.tpId = tpId;
    }

    @JsonProperty("koren-tsdn-topology:port-type")
    public String getPortType() {
        return portType;
    }

    @JsonProperty("koren-tsdn-topology:port-type")
    public void setPortType(String portType) {
        this.portType = portType;
    }
}
