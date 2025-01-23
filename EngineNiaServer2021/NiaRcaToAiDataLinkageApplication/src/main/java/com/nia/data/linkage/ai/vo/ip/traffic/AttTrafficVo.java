package com.nia.data.linkage.ai.vo.ip.traffic;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class AttTrafficVo implements Serializable {
    private String strifid;
    private String strifnm;
    private String strresid;
    private String strresnm;
    private String strtypemin;
    private String strifoperstatus;
    private int inttimestamp;
    private int intyear;
    private int intmonth;
    private int intday;
    private int inthour;
    private int intmin;
    private int intweek;
    private long intbandwidth;
    private float fltbpsin ;
    private float fltbpsout;
    private float fltppsin;
    private float fltppsout;
    private float flterrorin;
    private float flterrorout;
    private float fltdiscardin;
    private float fltdiscardout;
    private float fltunknown;
    private float fltusage;
    private float flhcinoctets;
    private float flhcoutoctets;
    private float flhcinucastpkts;
    private float flhcoutucastpkts;
    private float flhcinmulticastpkts;
    private float flhcoutmulticastpkts;
    private float flhcinbroadcastpkts;
    private float flhcoutbroadcastpkts;
    private float fltbpsinmax;
    private float fltbpsoutmax;
    private float fltppsinmax;
    private float fltppsoutmax;
    private float flterrorinmax;
    private float flterroroutmax;
    private float fltdiscardinmax;
    private float fltdiscardoutmax;
    private float fltunknownmax;
    private float fltusagemax;
    private boolean fltbpsin_anomaly;
    private boolean fltbpsout_anomaly;
    private String ai_accuracy;
    private Timestamp measured_datetime;

}
