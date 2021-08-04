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
public class PceOutputVo implements Serializable {

    @JsonProperty("output")
    private PceOutputDataVo pceOutputDataVo;

    @JsonProperty("output")
    public PceOutputDataVo getPceOutputDataVo() {
        return pceOutputDataVo;
    }

    @JsonProperty("output")
    public void setPceOutputDataVo(PceOutputDataVo pceOutputDataVo) {
        this.pceOutputDataVo = pceOutputDataVo;
    }
}
