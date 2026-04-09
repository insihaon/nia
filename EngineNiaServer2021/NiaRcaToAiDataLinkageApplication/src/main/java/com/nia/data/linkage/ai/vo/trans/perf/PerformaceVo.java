package com.nia.data.linkage.ai.vo.trans.perf;

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
    private double rxcur;
    private double rxmin;
    private double rxmax;
    private double rxave;
    private double txcur;
    private double txmin;
    private double txmax;
    private double txave;
    private String ocrtime;
}
