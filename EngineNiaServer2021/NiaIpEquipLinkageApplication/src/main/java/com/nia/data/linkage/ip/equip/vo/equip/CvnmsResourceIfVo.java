package com.nia.data.linkage.ip.equip.vo.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Data
@Scope(value = "prototype")
public class CvnmsResourceIfVo implements Serializable {
    private int intifidx;
    private int moduleSrl;
    private String strresid;
    private String strifid;
    private String strifname;
    private String strresidA;
    private String strresidZ;
    private long intifindex;
    private String strifdesc;
    private String striftype;
    private String strifspeed;
    private String strifphysaddress;
    private String strifoperstatus;
    private String striflimitbps;
    private String striflimitbpsc;
    private String striflimitbpsm;
    private String striflimitbpsn;
    private String striflimitbpsw;
    private String striferrlevel;
    private String strifipaddr;
    private String strifmanperf;
    private String strifmanerror;
    private String strifcurrent;
    private String striflimiterr;
    private String strifmemo;
    private String datelastupdatedate;
    private String dateregdate;


    @Override
    public String toString() {
        return "CvnmsResourceIfVo{" +
                "intifidx=" + intifidx +
                ", moduleSrl=" + moduleSrl +
                ", strresid='" + strresid + '\'' +
                ", strifid='" + strifid + '\'' +
                ", strifname='" + strifname + '\'' +
                ", strresidA='" + strresidA + '\'' +
                ", strresidZ='" + strresidZ + '\'' +
                ", intifindex=" + intifindex +
                ", strifdesc='" + strifdesc + '\'' +
                ", striftype='" + striftype + '\'' +
                ", strifspeed='" + strifspeed + '\'' +
                ", strifphysaddress='" + strifphysaddress + '\'' +
                ", strifoperstatus='" + strifoperstatus + '\'' +
                ", striflimitbps='" + striflimitbps + '\'' +
                ", striflimitbpsc='" + striflimitbpsc + '\'' +
                ", striflimitbpsm='" + striflimitbpsm + '\'' +
                ", striflimitbpsn='" + striflimitbpsn + '\'' +
                ", striflimitbpsw='" + striflimitbpsw + '\'' +
                ", striferrlevel='" + striferrlevel + '\'' +
                ", strifipaddr='" + strifipaddr + '\'' +
                ", strifmanperf='" + strifmanperf + '\'' +
                ", strifmanerror='" + strifmanerror + '\'' +
                ", strifcurrent='" + strifcurrent + '\'' +
                ", striflimiterr='" + striflimiterr + '\'' +
                ", strifmemo='" + strifmemo + '\'' +
                ", datelastupdatedate='" + datelastupdatedate + '\'' +
                ", dateregdate='" + dateregdate + '\'' +
                '}';
    }
}
