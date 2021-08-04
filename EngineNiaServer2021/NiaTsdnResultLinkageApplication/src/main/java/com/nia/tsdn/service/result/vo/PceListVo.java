package com.nia.tsdn.service.result.vo;

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
public class PceListVo implements Serializable {

    @JsonProperty("cost")
    private int cost;
    @JsonProperty("link")
    private List<PceListLinkVo> pceListLink;

    @JsonProperty("cost")
    public int getCost() {
        return cost;
    }

    @JsonProperty("cost")
    public void setCost(int cost) {
        this.cost = cost;
    }

    @JsonProperty("link")
    public List<PceListLinkVo> getPceListLink() {
        return pceListLink;
    }

    @JsonProperty("link")
    public void setPceListLink(List<PceListLinkVo> pceListLink) {
        this.pceListLink = pceListLink;
    }
}
