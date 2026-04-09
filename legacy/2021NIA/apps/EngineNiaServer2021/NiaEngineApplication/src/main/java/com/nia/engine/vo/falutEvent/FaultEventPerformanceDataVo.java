package com.nia.engine.vo.falutEvent;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component
@Scope(value = "prototype")
public class FaultEventPerformanceDataVo implements Serializable {
    private String sysname;
    private String port;
    private String unit;
    private String tmper;
    private String direction;
    private double rxCur;
    private double rxMin;
    private double rxMax;
    private double rxAve;
    private double txCur;
    private double txMin;
    private double txMax;
    private double txAve;
    private String ocrtime;
}
