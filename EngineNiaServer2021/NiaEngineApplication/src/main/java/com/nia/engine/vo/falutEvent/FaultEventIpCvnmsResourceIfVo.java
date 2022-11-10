package com.nia.engine.vo.falutEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component()
@Data
@Scope(value = "prototype")
public class FaultEventIpCvnmsResourceIfVo implements Serializable {
    private int intifidx;
    private int moduleSrl;
    private String strresid;
    private String strifid;
    private String strifname;
    private String strresidA;
    private String strresidZ;
    private int intifindex;
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

    @JsonProperty("module_srl")
    public int getModuleSrl() {
        return moduleSrl;
    }
}
