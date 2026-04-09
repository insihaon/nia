package com.nia.data.linkage.vo.performance.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Scope(value = "prototype")
public class PerformaceSourceVo implements Serializable {

    @JsonProperty("sid")
    private String sid;
    @JsonProperty("aid")
    private String aid;
    @JsonProperty("bid")
    private String bid;
    @JsonProperty("monsec")
    private String monsec;
    @JsonProperty("tmper")
    private String tmper;
    @JsonProperty("rxCur")
    private Double rxCur;
    @JsonProperty("rxMin")
    private Double rxMin;
    @JsonProperty("rxMax")
    private Double rxMax;
    @JsonProperty("rxAve")
    private Double rxAve;
    @JsonProperty("txCur")
    private Double txCur;
    @JsonProperty("txMin")
    private Double txMin;
    @JsonProperty("txMax")
    private Double txMax;
    @JsonProperty("txAve")
    private Double txAve;
    @JsonProperty("ocrdat")
    private String ocrdat;
    @JsonProperty("ocrtm")
    private String ocrtm;
    @JsonProperty("almType")
    private String almType;
    @JsonProperty("providerId")
    private String providerId;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("logTime")
    private String logTime;
    @JsonProperty("objectId")
    private String objectId;
    @JsonProperty("objectName")
    private String objectName;
    @JsonProperty("logType")
    private String logType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("date")
    private String date;
    @JsonProperty("source")
    private String source;
    @JsonProperty("reason")
    private String reason;

    @JsonProperty("sid")
    public String getSid() {
        return sid;
    }

    @JsonProperty("sid")
    public void setSid(String sid) {
        this.sid = sid;
    }

    @JsonProperty("aid")
    public String getAid() {
        return aid;
    }

    @JsonProperty("aid")
    public void setAid(String aid) {
        this.aid = aid;
    }

    @JsonProperty("bid")
    public String getBid() {
        return bid;
    }

    @JsonProperty("bid")
    public void setBid(String bid) {
        this.bid = bid;
    }

    @JsonProperty("monsec")
    public String getMonsec() {
        return monsec;
    }

    @JsonProperty("monsec")
    public void setMonsec(String monsec) {
        this.monsec = monsec;
    }

    @JsonProperty("tmper")
    public String getTmper() {
        return tmper;
    }

    @JsonProperty("tmper")
    public void setTmper(String tmper) {
        this.tmper = tmper;
    }

    @JsonProperty("rxCur")
    public Double getRxCur() {
        return rxCur;
    }

    @JsonProperty("rxCur")
    public void setRxCur(Double rxCur) {
        this.rxCur = rxCur;
    }

    @JsonProperty("rxMin")
    public Double getRxMin() {
        return rxMin;
    }

    @JsonProperty("rxMin")
    public void setRxMin(Double rxMin) {
        this.rxMin = rxMin;
    }

    @JsonProperty("rxMax")
    public Double getRxMax() {
        return rxMax;
    }

    @JsonProperty("rxMax")
    public void setRxMax(Double rxMax) {
        this.rxMax = rxMax;
    }

    @JsonProperty("rxAve")
    public Double getRxAve() {
        return rxAve;
    }

    @JsonProperty("rxAve")
    public void setRxAve(Double rxAve) {
        this.rxAve = rxAve;
    }

    @JsonProperty("txCur")
    public Double getTxCur() {
        return txCur;
    }

    @JsonProperty("txCur")
    public void setTxCur(Double txCur) {
        this.txCur = txCur;
    }

    @JsonProperty("txMin")
    public Double getTxMin() {
        return txMin;
    }

    @JsonProperty("txMin")
    public void setTxMin(Double txMin) {
        this.txMin = txMin;
    }

    @JsonProperty("txMax")
    public Double getTxMax() {
        return txMax;
    }

    @JsonProperty("txMax")
    public void setTxMax(Double txMax) {
        this.txMax = txMax;
    }

    @JsonProperty("txAve")
    public Double getTxAve() {
        return txAve;
    }

    @JsonProperty("txAve")
    public void setTxAve(Double txAve) {
        this.txAve = txAve;
    }

    @JsonProperty("ocrdat")
    public String getOcrdat() {
        return ocrdat;
    }

    @JsonProperty("ocrdat")
    public void setOcrdat(String ocrdat) {
        this.ocrdat = ocrdat;
    }

    @JsonProperty("ocrtm")
    public String getOcrtm() {
        return ocrtm;
    }

    @JsonProperty("ocrtm")
    public void setOcrtm(String ocrtm) {
        this.ocrtm = ocrtm;
    }

    @JsonProperty("almType")
    public String getAlmType() {
        return almType;
    }

    @JsonProperty("almType")
    public void setAlmType(String almType) {
        this.almType = almType;
    }

    @JsonProperty("providerId")
    public String getProviderId() {
        return providerId;
    }

    @JsonProperty("providerId")
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @JsonProperty("deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("deviceId")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("deviceName")
    public String getDeviceName() {
        return deviceName;
    }

    @JsonProperty("deviceName")
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @JsonProperty("logTime")
    public String getLogTime() {
        return logTime;
    }

    @JsonProperty("logTime")
    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    @JsonProperty("objectId")
    public String getObjectId() {
        return objectId;
    }

    @JsonProperty("objectId")
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @JsonProperty("objectName")
    public String getObjectName() {
        return objectName;
    }

    @JsonProperty("objectName")
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @JsonProperty("logType")
    public String getLogType() {
        return logType;
    }

    @JsonProperty("logType")
    public void setLogType(String logType) {
        this.logType = logType;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }
}

