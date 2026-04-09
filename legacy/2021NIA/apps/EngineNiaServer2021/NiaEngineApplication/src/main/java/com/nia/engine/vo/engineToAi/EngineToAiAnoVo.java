package com.nia.engine.vo.engineToAi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
import com.nia.engine.vo.sdn.factor.NodeFactorListVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficListVo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EngineToAiAnoVo {

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

    @JsonProperty("nren_id")
    private String nrenId;

    @JsonProperty("nren_name")
    private String nrenName;

    @JsonProperty("node_id")
    private String nodeId;

    @JsonProperty("if_id")
    private String ifId;

    @JsonProperty("strifid")
    private String strifid;

    @JsonProperty("strresid")
    private String strresid;

    @JsonProperty("strtypemin")
    private String strtypemin;

    @JsonProperty("inttimestamp")
    private int inttimestamp;

//    @JsonProperty("measured_datetime")
//    private Timestamp measuredDatetime;

    @JsonProperty("intyear")
    private int intyear;

    @JsonProperty("intmonth")
    private int intmonth;

    @JsonProperty("intday")
    private int intday;

    @JsonProperty("inthour")
    private int inthour;

    @JsonProperty("intmin")
    private int intmin;

    @JsonProperty("intweek")
    private int intweek;

    @JsonProperty("intbandwidth")
    private int intbandwidth;

    @JsonProperty("fltbpsin")
    private int fltbpsin;

    @JsonProperty("fltbpsout")
    private int fltbpsout;

    @JsonProperty("fltppsin")
    private int fltppsin;

    @JsonProperty("fltppsout")
    private int fltppsout;

    @JsonProperty("flterrorin")
    private int flterrorin;

    @JsonProperty("flterrorout")
    private int flterrorout;

    @JsonProperty("fltdiscardin")
    private int fltdiscardin;

    @JsonProperty("fltdiscardout")
    private int fltdiscardout;

    @JsonProperty("fltunknown")
    private int fltunknown;

    @JsonProperty("fltusage")
    private int fltusage;

    @JsonProperty("flhcinoctets")
    private int flhcinoctets;

    @JsonProperty("flhcoutoctets")
    private int flhcoutoctets;

    @JsonProperty("flhcinucastpkts")
    private int flhcinucastpkts;

    @JsonProperty("flhcoutucastpkts")
    private int flhcoutucastpkts;

    @JsonProperty("flhcinmulticastpkts")
    private int flhcinmulticastpkts;

    @JsonProperty("flhcoutmulticastpkts")
    private int flhcoutmulticastpkts;

    @JsonProperty("flhcinbroadcastpkts")
    private int flhcinbroadcastpkts;

    @JsonProperty("flhcoutbroadcastpkts")
    private int flhcoutbroadcastpkts;

    @JsonProperty("fltbpsinmax")
    private int fltbpsinmax;

    @JsonProperty("fltbpsoutmax")
    private int fltbpsoutmax;

    @JsonProperty("fltppsinmax")
    private int fltppsinmax;

    @JsonProperty("fltppsoutmax")
    private int fltppsoutmax;

    @JsonProperty("flterrorinmax")
    private int flterrorinmax;

    @JsonProperty("flterroroutmax")
    private int flterroroutmax;

    @JsonProperty("fltdiscardinmax")
    private int fltdiscardinmax;

    @JsonProperty("fltdiscardoutmax")
    private int fltdiscardoutmax;

    @JsonProperty("fltunknownmax")
    private int fltunknownmax;

    @JsonProperty("fltusagemax")
    private int fltusagemax;

    @JsonProperty("ai_accuracy")
    private String aiAccuracy;




}
