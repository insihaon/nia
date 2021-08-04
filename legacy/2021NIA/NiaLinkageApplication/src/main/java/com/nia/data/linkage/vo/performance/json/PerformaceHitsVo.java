package com.nia.data.linkage.vo.performance.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Scope(value = "prototype")
public class PerformaceHitsVo implements Serializable {

    @JsonProperty("hits")
    private List<PerformaceHitsListVo> performaceHitsListVoList;

    @JsonProperty("hits")
    public List<PerformaceHitsListVo> getPerformaceHitsListVoList() {
        return performaceHitsListVoList;
    }

    @JsonProperty("hits")
    public void setPerformaceHitsListVoList(List<PerformaceHitsListVo> performaceHitsListVoList) {
        this.performaceHitsListVoList = performaceHitsListVoList;
    }
}
