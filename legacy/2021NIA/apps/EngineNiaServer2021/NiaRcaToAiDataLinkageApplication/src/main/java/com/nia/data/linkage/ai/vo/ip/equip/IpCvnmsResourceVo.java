package com.nia.data.linkage.ai.vo.ip.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Data
@Scope(value = "prototype")
public class IpCvnmsResourceVo implements Serializable {
    private int intresidx;
    private int module_srl;
    private int member_srl;
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
    private String strsnmpv3_username;
    private String strsnmpv3_securitylevel;
    private String strsnmpv3_authenticationprotocol;
    private String strsnmpv3_authenticationpassword;
    private String strsnmpv3_privacyprotocol;
    private String strsnmpv3_privacypassword;
    private String strsnmpv3_contextname;
    private String strenabled_cpu;
    private String strenabled_mem;
    private String strenabled_disk;
    private String strsnmpmib_cpu;
    private String strsnmpmib_mem;
    private String strsnmpmib_disk;
    private String strevent_cpu;
    private String strevent_mem;
    private String strevent_disk;
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
