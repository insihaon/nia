package com.nia.engine.vo.falutEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Data
@Scope(value = "prototype")
public class FaultEventIpCvnmsResourceVo implements Serializable {
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

    @JsonProperty("module_srl")
    public int getModuleSrl() {
        return moduleSrl;
    }
    @JsonProperty("stracknowncheck_member_srl")
    public int getMemberSrl() {
        return memberSrl;
    }
    @JsonProperty("strsnmpv3_username")
    public String getStrsnmpv3Username() {
        return strsnmpv3Username;
    }
    @JsonProperty("strsnmpv3_securitylevel")
    public String getStrsnmpv3Securitylevel() {
        return strsnmpv3Securitylevel;
    }
    @JsonProperty("strsnmpv3Authenticationprotocol")
    public String getStrsnmpv3Authenticationprotocol() {
        return strsnmpv3Authenticationprotocol;
    }
    @JsonProperty("strsnmpv3_authenticationpassword")
    public String getStrsnmpv3Authenticationpassword() {
        return strsnmpv3Authenticationpassword;
    }
    @JsonProperty("strsnmpv3_privacyprotocol")
    public String getStrsnmpv3Privacyprotocol() {
        return strsnmpv3Privacyprotocol;
    }
    @JsonProperty("strsnmpv3_privacypassword")
    public String getStrsnmpv3Privacypassword() {
        return strsnmpv3Privacypassword;
    }
    @JsonProperty("strsnmpv3_contextname")
    public String getStrsnmpv3Contextname() {
        return strsnmpv3Contextname;
    }
    @JsonProperty("strenabled_cpu")
    public String getStrenabledCpu() {
        return strenabledCpu;
    }
    @JsonProperty("strenabled_mem")
    public String getStrenabledMem() {
        return strenabledMem;
    }
    @JsonProperty("strenabled_disk")
    public String getStrenabledDisk() {
        return strenabledDisk;
    }
    @JsonProperty("strsnmpmib_cpu")
    public String getStrsnmpmibCpu() {
        return strsnmpmibCpu;
    }
    @JsonProperty("strsnmpmib_mem")
    public String getStrsnmpmibMem() {
        return strsnmpmibMem;
    }
    @JsonProperty("strsnmpmib_disk")
    public String getStrsnmpmibDisk() {
        return strsnmpmibDisk;
    }
    @JsonProperty("strevent_cpu")
    public String getStreventCpu() {
        return streventCpu;
    }
    @JsonProperty("strevent_mem")
    public String getStreventMem() {
        return streventMem;
    }
    @JsonProperty("strevent_disk")
    public String getStreventDisk() {
        return streventDisk;
    }
}
