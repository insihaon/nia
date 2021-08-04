package com.nia.data.linkage.vo.alarm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Scope(value = "prototype")
public class AlarmDataVo implements Serializable {

    @JsonProperty("almType")
    private String almType;
    @JsonProperty("sid")
    private String sid;
    @JsonProperty("aid")
    private String aid;
    @JsonProperty("sev")
    private String sev;
    @JsonProperty("bid")
    private String bid;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("almcde")
    private String almcde;
    @JsonProperty("srveff")
    private String srveff;
    @JsonProperty("ocrdat")
    private String ocrdat;
    @JsonProperty("ocrtm")
    private String ocrtm;
    @JsonProperty("providerId")
    private String providerId;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("logTime")
    private String logTime;
    @JsonProperty("severity")
    private String severity;
    @JsonProperty("objectId")
    private String objectId;
    @JsonProperty("objectName")
    private String objectName;
    @JsonProperty("logType")
    private String logType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("level")
    private String level;
    @JsonProperty("date")
    private String date;
    @JsonProperty("source")
    private String source;
    @JsonProperty("reason")
    private String reason;

    @JsonProperty("almType")
    public String getAlmType() {
        return almType;
    }

    @JsonProperty("almType")
    public void setAlmType(String almType) {
        this.almType = almType;
    }

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

    @JsonProperty("sev")
    public String getSev() {
        return sev;
    }

    @JsonProperty("sev")
    public void setSev(String sev) {
        this.sev = sev;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("bid")
    public String getBid() {
        return bid;
    }

    @JsonProperty("bid")
    public void setBid(String bid) {
        this.bid = bid;
    }

    @JsonProperty("almcde")
    public String getAlmcde() {
        return almcde;
    }

    @JsonProperty("almcde")
    public void setAlmcde(String almcde) {
        this.almcde = almcde;
    }

    @JsonProperty("srveff")
    public String getSrveff() {
        return srveff;
    }

    @JsonProperty("srveff")
    public void setSrveff(String srveff) {
        this.srveff = srveff;
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

    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    @JsonProperty("severity")
    public void setSeverity(String severity) {
        this.severity = severity;
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

    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(String level) {
        this.level = level;
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
