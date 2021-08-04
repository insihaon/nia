package com.nia.ai.per.ap.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component
@Scope(value = "prototype")
public class RoadmPerformanceOrgVo implements Serializable {

    private String trunkId;
    private String sysname;
    private String roadmCode;
    private String port;
    private String unit;
    private String tmper;
    private double inValueCur;
    private double inValueMin;
    private double inValueMax;
    private double inValueAve;
    private double outValueCur;
    private double outValueMin;
    private double outValueMax;
    private double outValueAve;
    private Timestamp ocrtime;
    private int routeNum;
    private String direction;
    private Boolean isInRowSignal;
    private Boolean isOutRowSignal;

    public void setRoadmPerformanceOrgVo(RoadmPerformanceOrgVo roadmPerformanceOrgVo) {
        this.trunkId = roadmPerformanceOrgVo.getTrunkId();
        this.sysname = roadmPerformanceOrgVo.getSysname();
        this.roadmCode = roadmPerformanceOrgVo.getRoadmCode();
        this.port = roadmPerformanceOrgVo.getPort();
        this.unit = roadmPerformanceOrgVo.getUnit();
        this.tmper = roadmPerformanceOrgVo.getTmper();
        this.inValueCur = roadmPerformanceOrgVo.getInValueCur();
        this.inValueMin = roadmPerformanceOrgVo.getInValueMin();
        this.inValueMax = roadmPerformanceOrgVo.getInValueMax();
        this.inValueAve = roadmPerformanceOrgVo.getInValueAve();
        this.outValueCur = roadmPerformanceOrgVo.getOutValueCur();
        this.outValueMin = roadmPerformanceOrgVo.getOutValueMin();
        this.outValueMax = roadmPerformanceOrgVo.getOutValueMax();
        this.outValueAve = roadmPerformanceOrgVo.getOutValueAve();
        this.ocrtime = roadmPerformanceOrgVo.getOcrtime();
        this.routeNum = roadmPerformanceOrgVo.getRouteNum();
        this.direction = roadmPerformanceOrgVo.getDirection();
        this.isInRowSignal = roadmPerformanceOrgVo.getIsInRowSignal();
        this.isOutRowSignal = roadmPerformanceOrgVo.getIsOutRowSignal();
    }
}
