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
public class ServicePceDataVo implements Serializable {

    @JsonProperty("cost")
    private int cost;
    @JsonProperty("link")
    private List<ServicePceLinkDataVo> servicePceLinkDataVoList;

    @JsonProperty("cost")
    public int getCost() {
        return cost;
    }

    @JsonProperty("cost")
    public void setCost(int cost) {
        this.cost = cost;
    }

    @JsonProperty("link")
    public List<ServicePceLinkDataVo> getServicePceLinkDataVoList() {
        return servicePceLinkDataVoList;
    }

    @JsonProperty("link")
    public void setServicePceLinkDataVoList(List<ServicePceLinkDataVo> servicePceLinkDataVoList) {
        this.servicePceLinkDataVoList = servicePceLinkDataVoList;
    }
}
