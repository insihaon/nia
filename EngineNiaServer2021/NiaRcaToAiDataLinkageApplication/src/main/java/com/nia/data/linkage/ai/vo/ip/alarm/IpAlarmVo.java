package com.nia.data.linkage.ai.vo.ip.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class IpAlarmVo implements Serializable {
    private String strresname;
    private String stripaddr;
    private String strifname;
    private String strifdesc;
    private String strifspeed;
    private String striftype;
    private String strifoperstatus;
    private int interridx;
    private int module_srl;
    private String strresid;
    private String strifid;
    private String strtype;
    private String strlevel;
    private String strtitle;
    private String strcontent;
    private String strgroupid;
    private String strgroupid1;
    private String strgroupid2;
    private String strgroupid3;
    private String strgroupid4;
    private String strgroupid5;
    private String strgroupid6;
    private String strgroupid7;
    private String strgroupid8;
    private String strgroupid9;
    private String strgroupid10;
    private String dateraisedate;
    private String datecleardate;
    private String dateregdate;
    private String strclearcheck;
    private String strmemo;
    private String stracknowncheck;
    private int stracknowncheck_member_srl;
}
