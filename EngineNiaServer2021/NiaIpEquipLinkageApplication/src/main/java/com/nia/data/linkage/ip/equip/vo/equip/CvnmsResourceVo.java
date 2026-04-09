package com.nia.data.linkage.ip.equip.vo.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Data
@Scope(value = "prototype")
public class CvnmsResourceVo implements Serializable {
    private int intresidx;
    private int moduleSrl;
    private int memberSrl;
    private String strresname;
    private String strresid;
    private String strrestype;
    private String strmodelid;
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
    private String strtags;
    private String strpriority;
    private String strresmemo;
    private String strcolid;
    private String stripver;
    private String stripaddr;
    private String stripv6addr;
    private String strsnmpipaddr;
    private String strsnmpver;
    private String strsnmpcommunity;
    private int intsnmpport;
    private int intsnmptimeout;
    private String strsnmpv3Username;
    private String strsnmpv3Securitylevel;
    private String strsnmpv3Authenticationprotocol;
    private String strsnmpv3Authenticationpassword;
    private String strsnmpv3Privacyprotocol;
    private String strsnmpv3Privacypassword;
    private String strsnmpv3Contextname;
    private String strenabledCpu;
    private String strenabledMem;
    private String strenabledDisk;
    private String strsnmpmibCpu;
    private String strsnmpmibMem;
    private String strsnmpmibDisk;
    private String streventCpu;
    private String streventMem;
    private String streventDisk;
    private String strstatussnmp;
    private String strenabledsnmp;
    private String dateuptime;
    private String strstatusping;
    private int intintervalping;
    private int inttimeoutping;
    private String strenabledping;
    private int intlograisecount;
    private int interrraisecount;
    private String strcpucurrent;
    private String strmemcurrent;
    private String strdiskcurrent;
    private String strrttcurrent;
    private String intsensorpid;
    private String stralive;
    private String strcontacts;
    private String dateprobeupdatedate;
    private String datelastupdatedate;
    private String dateregdate;
}
