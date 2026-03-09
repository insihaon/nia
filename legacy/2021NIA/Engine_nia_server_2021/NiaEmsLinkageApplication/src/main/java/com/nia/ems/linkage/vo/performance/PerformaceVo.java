package com.nia.ems.linkage.vo.performance;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class PerformaceVo implements Serializable {
    private String sysname;
    private String port;
    private String unit;
    private String tmper;
    private double rxCur;
    private double rxMin;
    private double rxMax;
    private double rxAve;
    private double txCur;
    private double txMin;
    private double txMax;
    private double txAve;
    private Timestamp ocrtime;
}
