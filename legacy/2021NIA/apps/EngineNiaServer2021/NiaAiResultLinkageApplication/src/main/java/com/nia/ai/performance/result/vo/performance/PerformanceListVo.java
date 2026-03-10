package com.nia.ai.performance.result.vo.performance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerformanceListVo implements Serializable {
    @JsonProperty("data")
    private List<PerformanceVo> data;

    @JsonProperty("data")
    public List<PerformanceVo> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<PerformanceVo> data) {
        this.data = data;
    }
}
