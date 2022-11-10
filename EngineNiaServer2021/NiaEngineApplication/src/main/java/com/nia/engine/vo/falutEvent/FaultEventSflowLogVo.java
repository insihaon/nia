package com.nia.engine.vo.falutEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class FaultEventSflowLogVo implements Serializable {
    private String strresip;
    private String strresname;
    private String strsIp;
    private String strsPort;
    private String strdIp;
    private String strdPort;
    private String strsMac;
    private String strdMac;
    private String strprotocol;
    private String stripv4tos;
    private String strchannel;
    private String strsenderip;
    private String strinInterface;
    private String stroutInterface;
    private String strbytesCol;
    private String strcounts;
    private String dateregdate;

    @JsonProperty("strresip")
    public String getStrresip() {
        return strresip;
    }
    @JsonProperty("strresname")
    public String getStrresname() {
        return strresname;
    }
    @JsonProperty("strs_ip")
    public String getStrsIp() {
        return strsIp;
    }
    @JsonProperty("strs_port")
    public String getStrsPort() {
        return strsPort;
    }
    @JsonProperty("strd_ip")
    public String getStrdIp() {
        return strdIp;
    }
    @JsonProperty("strd_port")
    public String getStrdPort() {
        return strdPort;
    }
    @JsonProperty("strs_mac")
    public String getStrsMac() {
        return strsMac;
    }
    @JsonProperty("strd_mac")
    public String getStrdMac() {
        return strdMac;
    }
    @JsonProperty("strprotocol")
    public String getStrprotocol() {
        return strprotocol;
    }
    @JsonProperty("stripv4tos")
    public String getStripv4tos() {
        return stripv4tos;
    }
    @JsonProperty("strchannel")
    public String getStrchannel() {
        return strchannel;
    }
    @JsonProperty("strsenderip")
    public String getStrsenderip() {
        return strsenderip;
    }
    @JsonProperty("strin_interface")
    public String getStrinInterface() {
        return strinInterface;
    }
    @JsonProperty("strout_interface")
    public String getStroutInterface() {
        return stroutInterface;
    }
    @JsonProperty("strbytes_col")
    public String getStrbytesCol() {
        return strbytesCol;
    }
    @JsonProperty("strcounts")
    public String getStrcounts() {
        return strcounts;
    }
    @JsonProperty("dateregdate")
    public String getDateregdate() {
        return dateregdate;
    }
}
