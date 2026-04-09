package com.nia.data.linkage.vo.performance.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Scope(value = "prototype")
public class PerformaceHitsListVo implements Serializable {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("_source")
    private PerformaceSourceVo performaceSourceVo;

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("_source")
    public PerformaceSourceVo getPerformaceSourceVo() {
        return performaceSourceVo;
    }

    @JsonProperty("_source")
    public void setPerformaceSourceVo(PerformaceSourceVo performaceSourceVo) {
        this.performaceSourceVo = performaceSourceVo;
    }
}
