package com.nia.engine.vo.aiToEngine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiToEngineAnoVo {

    @JsonProperty("ticket_id")
    private String ticketId;
    @JsonProperty("ticket_type")
    private String ticketType;
    private String strresnm;
    private String stripaddr;
    private String strifname;
    private String strifdesc;
    private String strifspeed;
    private String striftype;
    private String strifoperstatus;
    private String organId;//
    private String organName;
    @JsonProperty("node_id")
    private String nodeId;//
    @JsonProperty("if_id")
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
    private int intbandwidth;
    private int fltbpsin;
    private int fltbpsout;
    private int fltppsin;
    private int fltppsout;
    private int flterrorin;
    private int flterrorout;
    private int fltdiscardin;
    private int fltdiscardout;
    private int fltunknown;
    private int fltusage;
    private int flhcinoctets;
    private int flhcoutoctets;
    private int flhcinucastpkts;
    private int flhcoutucastpkts;
    private int flhcinmulticastpkts;
    private int flhcoutmulticastpkts;
    private int flhcinbroadcastpkts;
    private int flhcoutbroadcastpkts;
    private int fltbpsinmax;
    private int fltbpsoutmax;
    private int fltppsinmax;
    private int fltppsoutmax;
    private int flterrorinmax;
    private int flterroroutmax;
    private int fltdiscardinmax;
    private int fltdiscardoutmax;
    private int fltunknownmax;
    private int fltusagemax;
    @JsonProperty("ai_accuracy")
    private String aiAccuracy;
    @JsonProperty("zero1_model")
    private int zero1Model;
    @JsonProperty("zero1_entropy")
    private double zero1Entropy;



}
