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
public class ServicePceInputJsonVo implements Serializable {
    @JsonProperty("input")
    private ServicePceInputDataJsonVo servicePceJsonVo;

    @JsonProperty("input")
    public ServicePceInputDataJsonVo getServicePceJsonVo() {
        return servicePceJsonVo;
    }

    @JsonProperty("input")
    public void setServicePceJsonVo(ServicePceInputDataJsonVo servicePceJsonVo) {
        this.servicePceJsonVo = servicePceJsonVo;
    }
}
