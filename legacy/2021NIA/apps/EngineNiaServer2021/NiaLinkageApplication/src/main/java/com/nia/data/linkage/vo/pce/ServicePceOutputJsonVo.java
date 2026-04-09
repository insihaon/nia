package com.nia.data.linkage.vo.pce;

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
public class ServicePceOutputJsonVo implements Serializable {
    @JsonProperty("output")
    private ServicePceOutputDataJsonVo servicePceOutputJsonDataVo;

    @JsonProperty("output")
    public ServicePceOutputDataJsonVo getServicePceOutputJsonDataVo() {
        return servicePceOutputJsonDataVo;
    }

    @JsonProperty("output")
    public void setServicePceOutputJsonDataVo(ServicePceOutputDataJsonVo servicePceOutputJsonDataVo) {
        this.servicePceOutputJsonDataVo = servicePceOutputJsonDataVo;
    }
}
