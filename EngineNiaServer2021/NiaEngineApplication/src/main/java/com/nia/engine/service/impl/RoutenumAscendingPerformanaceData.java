package com.nia.engine.service.impl;

import com.nia.engine.vo.RoadmPerformanceOrgVo;

import java.util.Comparator;

class RoutenumAscendingPerformanaceData implements Comparator<RoadmPerformanceOrgVo> {

    @Override
    public int compare(RoadmPerformanceOrgVo o1, RoadmPerformanceOrgVo o2) {
        return new Integer(o1.getRouteNum()).compareTo(new Integer(o2.getRouteNum()));
    }
}

