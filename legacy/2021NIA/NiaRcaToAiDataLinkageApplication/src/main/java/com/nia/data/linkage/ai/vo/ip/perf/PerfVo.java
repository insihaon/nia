package com.nia.data.linkage.ai.vo.ip.perf;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class PerfVo implements Serializable {
    private String strIfID;
    private String strResID;
    private String strTypeMin;
    private int intTimestamp;
    private int intYear;
    private int intMonth;
    private int intDay;
    private int intHour;
    private int intMin;
    private int intWeek;
    private long intBandwidth;
    private double fltBPSIn;
    private double fltBPSOut;
    private double fltPPSIn;
    private double fltPPSOut;
    private double fltErrorIn;
    private double fltErrorOut;
    private double fltDiscardIn;
    private double fltDiscardOut;
    private double fltUnknown;
    private double fltUsage;
    private double flHCInOctets;
    private double flHCOutOctets;
    private double flHCInUcastPkts;
    private double flHCOutUcastPkts;
    private double flHCInMulticastPkts;
    private double flHCOutMulticastPkts;
    private double flHCInBroadcastPkts;
    private double flHCOutBroadcastPkts;
    private double fltBPSInMax;
    private double fltBPSOutMax;
    private double fltPPSInMax;
    private double fltPPSOutMax;
    private double fltErrorInMax;
    private double fltErrorOutMax;
    private double fltDiscardInMax;
    private double fltDiscardOutMax;
    private double fltUnknownMax;
    private double fltUsageMax;
}
