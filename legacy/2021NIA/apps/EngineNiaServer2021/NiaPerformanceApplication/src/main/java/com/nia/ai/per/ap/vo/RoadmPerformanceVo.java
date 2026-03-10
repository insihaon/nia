package com.nia.ai.per.ap.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component
@Scope(value = "prototype")
public class RoadmPerformanceVo implements Serializable {

    private String monitoredObject;
    private String performanaceEvent;
    private String sysname;
    private String roadmName;
    private int routeNum;
    private String port;
    private Timestamp endTime;
    private double valueMax;
    private double valueCur;
    private double valueMin;
    private double valueAvr;
    private String direction;
    private int inOutNum;
}
