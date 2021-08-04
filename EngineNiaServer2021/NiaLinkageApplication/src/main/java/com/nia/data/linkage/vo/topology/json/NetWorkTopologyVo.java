package com.nia.data.linkage.vo.topology.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.data.linkage.vo.topology.json.service.ServiceDataVo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class NetWorkTopologyVo implements Serializable {

    @JsonProperty("topology")
    private List<TopologyListVo> topologyListVo;

    @JsonProperty("koren-tsdn-topology:service")
    private List<ServiceDataVo> serviceDataVoList;

    @JsonProperty("topology")
    public List<TopologyListVo> getTopologyListVo() {
        return topologyListVo;
    }

    @JsonProperty("topology")
    public void setTopologyListVo(List<TopologyListVo> topologyListVo) {
        this.topologyListVo = topologyListVo;
    }

    @JsonProperty("koren-tsdn-topology:service")
    public List<ServiceDataVo> getServiceDataVoList() {
        return serviceDataVoList;
    }

    @JsonProperty("koren-tsdn-topology:service")
    public void setServiceDataVoList(List<ServiceDataVo> serviceDataVoList) {
        this.serviceDataVoList = serviceDataVoList;
    }
}
