package com.nia.data.linkage.vo.pce;

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
public class ServicePceOutputPceJsonVo implements Serializable {

    @JsonProperty("link")
    private List<ServicePceOutputPceLinkDataJsonVo> servicePceOutputPceLinkDataJsonVoList;

    @JsonProperty("cost")
    private int cost;

    @JsonProperty("link")
    public List<ServicePceOutputPceLinkDataJsonVo> getServicePceOutputPceLinkDataJsonVoList() {
        return servicePceOutputPceLinkDataJsonVoList;
    }

    @JsonProperty("link")
    public void setServicePceOutputPceLinkDataJsonVoList(List<ServicePceOutputPceLinkDataJsonVo> servicePceOutputPceLinkDataJsonVoList) {
        this.servicePceOutputPceLinkDataJsonVoList = servicePceOutputPceLinkDataJsonVoList;
    }

    @JsonProperty("cost")
    public int getCost() {
        return cost;
    }

    @JsonProperty("cost")
    public void setCost(int cost) {
        this.cost = cost;
    }
}
