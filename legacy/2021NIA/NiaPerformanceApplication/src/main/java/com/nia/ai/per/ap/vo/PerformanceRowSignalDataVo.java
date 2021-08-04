package com.nia.ai.per.ap.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Scope(value = "prototype")
public class PerformanceRowSignalDataVo {

    private List<RoadmPerformanceOrgVo> downPerformanaceList;
    private List<RoadmPerformanceOrgVo> upPerformanaceList;

}
