package com.nia.ai.per.ap.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component
@Scope(value = "prototype")
public class PerformanceClusterVo implements Serializable {
    private String trunkId;
    private String direction;
    private List<RoadmPerformanceOrgVo> roadmPerformanceList;
}
