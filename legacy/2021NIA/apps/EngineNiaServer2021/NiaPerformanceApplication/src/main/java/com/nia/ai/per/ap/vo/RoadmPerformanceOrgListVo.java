package com.nia.ai.per.ap.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoadmPerformanceOrgListVo {

    @JsonProperty("data")
    private List<RoadmPerformanceOrgVo> roadmPerformanaceOrgList;

    @JsonProperty("data")
    public List<RoadmPerformanceOrgVo> getRoadmPerformanaceOrgList() {
        return roadmPerformanaceOrgList;
    }

    @JsonProperty("data")
    public void setRoadmPerformanaceOrgList(List<RoadmPerformanceOrgVo> roadmPerformanaceOrgList) {
        this.roadmPerformanaceOrgList = roadmPerformanaceOrgList;
    }
}
