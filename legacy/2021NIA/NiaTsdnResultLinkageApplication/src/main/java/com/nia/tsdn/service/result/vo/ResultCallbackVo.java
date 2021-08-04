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
public class ResultCallbackVo implements Serializable {

    @JsonProperty("ok-url")
    private String okUrl;
    @JsonProperty("fail-url")
    private String failUrl;

    @JsonProperty("ok-url")
    public String getOkUrl() {
        return okUrl;
    }

    @JsonProperty("ok-url")
    public void setOkUrl(String okUrl) {
        this.okUrl = okUrl;
    }

    @JsonProperty("fail-url")
    public String getFailUrl() {
        return failUrl;
    }

    @JsonProperty("fail-url")
    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }
}
