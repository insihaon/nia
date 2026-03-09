package com.nia.data.linkage.vo.performance.json;

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
public class PerformaceJonDataVo implements Serializable {

    @JsonProperty("hits")
    private PerformaceHitsVo performaceHitsVo;

    @JsonProperty("hits")
    public PerformaceHitsVo getPerformaceHitsVo() {
        return performaceHitsVo;
    }

    @JsonProperty("hits")
    public void setPerformaceHitsVo(PerformaceHitsVo performaceHitsVo) {
        this.performaceHitsVo = performaceHitsVo;
    }
}

