package com.nia.data.linkage.vo.topology.json;

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
public class TopologyDataVo implements Serializable {

    @JsonProperty("network-topology")
    private NetWorkTopologyVo netWorkTopologyVo;

    @JsonProperty("network-topology")
    public NetWorkTopologyVo getNetWorkTopologyVo() {
        return netWorkTopologyVo;
    }

    @JsonProperty("network-topology")
    public void setNetWorkTopologyVo(NetWorkTopologyVo netWorkTopologyVo) {
        this.netWorkTopologyVo = netWorkTopologyVo;
    }
}
