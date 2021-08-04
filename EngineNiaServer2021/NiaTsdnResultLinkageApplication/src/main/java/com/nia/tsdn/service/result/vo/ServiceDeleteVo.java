package com.nia.tsdn.service.result.vo;

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
public class ServiceDeleteVo implements Serializable {

    @JsonProperty("provider-id")
    private String providerId;
    @JsonProperty("service-id")
    private String serviceId;
    @JsonProperty("result-callback")
    private ResultCallbackVo resultCallbackVo;

    @JsonProperty("provider-id")
    public String getProviderId() {
        return providerId;
    }

    @JsonProperty("provider-id")
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @JsonProperty("service-id")
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty("service-id")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @JsonProperty("result-callback")
    public ResultCallbackVo getResultCallbackVo() {
        return resultCallbackVo;
    }

    @JsonProperty("result-callback")
    public void setResultCallbackVo(ResultCallbackVo resultCallbackVo) {
        this.resultCallbackVo = resultCallbackVo;
    }
}
