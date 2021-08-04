package com.nia.ai.performance.result.vo.performance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component()
@Scope(value = "prototype")
public class PerformanceVo implements Serializable {
    @JsonProperty("sysname")
    private String sysname;
    @JsonProperty("port")
    private String port;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("tmper")
    private String tmper;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("rxcur")
    private double rxcur;
    @JsonProperty("rxmin")
    private double rxmin;
    @JsonProperty("rxmax")
    private double rxmax;
    @JsonProperty("rxave")
    private double rxave;
    @JsonProperty("txcur")
    private double txcur;
    @JsonProperty("txmin")
    private double txmin;
    @JsonProperty("txmax")
    private double txmax;
    @JsonProperty("txave")
    private double txave;
    @JsonProperty("ocrtime")
    private String ocrtime;

    @JsonProperty("sysname")
    public String getSysname() {
        return sysname;
    }

    @JsonProperty("sysname")
    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    @JsonProperty("port")
    public String getPort() {
        return port;
    }

    @JsonProperty("port")
    public void setPort(String port) {
        this.port = port;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("tmper")
    public String getTmper() {
        return tmper;
    }

    @JsonProperty("tmper")
    public void setTmper(String tmper) {
        this.tmper = tmper;
    }

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonProperty("rxcur")
    public double getRxcur() {
        return rxcur;
    }

    @JsonProperty("rxcur")
    public void setRxcur(String rxcur) {
        this.rxcur = Double.parseDouble(rxcur);
    }

    @JsonProperty("rxmin")
    public double getRxmin() {
        return rxmin;
    }

    @JsonProperty("rxmin")
    public void setRxmin(String rxmin) {
        this.rxmin = Double.parseDouble(rxmin);
    }

    @JsonProperty("rxmax")
    public double getRxmax() {
        return rxmax;
    }

    @JsonProperty("rxmax")
    public void setRxmax(String rxmax) {
        this.rxmax = Double.parseDouble(rxmax);
    }

    @JsonProperty("rxave")
    public double getRxave() {
        return rxave;
    }

    @JsonProperty("rxave")
    public void setRxave(String rxave) {
        this.rxave = Double.parseDouble(rxave);
    }

    @JsonProperty("txcur")
    public double getTxcur() {
        return txcur;
    }

    @JsonProperty("txcur")
    public void setTxcur(String txcur) {
        this.txcur = Double.parseDouble(txcur);
    }

    @JsonProperty("txmin")
    public double getTxmin() {
        return txmin;
    }

    @JsonProperty("txmin")
    public void setTxmin(String txmin) {
        this.txmin = Double.parseDouble(txmin);
    }

    @JsonProperty("txmax")
    public double getTxmax() {
        return txmax;
    }

    @JsonProperty("txmax")
    public void setTxmax(String txmax) {
        this.txmax = Double.parseDouble(txmax);
    }

    @JsonProperty("txave")
    public double getTxave() {
        return txave;
    }

    @JsonProperty("txave")
    public void setTxave(String txave) {
        this.txave = Double.parseDouble(txave);
    }

    @JsonProperty("ocrtime")
    public String getOcrtime() {
        return ocrtime;
    }

    @JsonProperty("ocrtime")
    public void setOcrtime(String ocrtime) {
        this.ocrtime = ocrtime;
    }
}
