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
public class PceInputVo implements Serializable {

    @JsonProperty("input")
    private PceInputDataVo pceInputDataVo;

    @JsonProperty("input")
    public PceInputDataVo getPceInputDataVo() {
        return pceInputDataVo;
    }

    @JsonProperty("input")
    public void setPceInputDataVo(PceInputDataVo pceInputDataVo) {
        this.pceInputDataVo = pceInputDataVo;
    }
}
