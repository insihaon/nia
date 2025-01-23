package com.nia.engine.vo.falutEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class FaultEventPerfVo implements Serializable {
    private String strresname;
    private String stripaddr;
    private String strifname;
    private String strifdesc;
    private String strifspeed;
    private String striftype;
    private String strifoperstatus;
    private String nrenId;
    private String nrenName;
    private String nodeId;
    private String ifId;
    private String strifid;
    private String strresid;
    private String strtypemin;
    private int inttimestamp;
    private int intyear;
    private int intmonth;
    private int intday;
    private int inthour;
    private int intmin;
    private int intweek;
    private long intbandwidth;
    private double fltbpsin;
    private double fltbpsout;
    private double fltppsin;
    private double fltppsout;
    private double flterrorin;
    private double flterrorout;
    private double fltdiscardin;
    private double fltdiscardout;
    private double fltunknown;
    private double fltusage;
    private double flhcinoctets;
    private double flhcoutoctets;
    private double flhcinucastpkts;
    private double flhcoutucastpkts;
    private double flhcinmulticastpkts;
    private double flhcoutmulticastpkts;
    private double flhcinbroadcastpkts;
    private double flhcoutbroadcastpkts;
    private double fltbpsinmax;
    private double fltbpsoutmax;
    private double fltppsinmax;
    private double fltppsoutmax;
    private double flterrorinmax;
    private double flterroroutmax;
    private double fltdiscardinmax;
    private double fltdiscardoutmax;
    private double fltunknownmax;
    private double fltusagemax;
    private String aiAccuracy;


    @JsonProperty("nren_id")
    public String getnrenId() {
        return nrenId;
    }
    @JsonProperty("nren_name")
    public String getnrenName() {
        return nrenName;
    }
    @JsonProperty("node_id")
    public String getNodeId() {
        return nodeId;
    }
    @JsonProperty("if_id")
    public String getIfId() {
        return ifId;
    }
    @JsonProperty("ai_accuracy")
    public String getAiAccuracy() {
        return aiAccuracy;
    }
}
