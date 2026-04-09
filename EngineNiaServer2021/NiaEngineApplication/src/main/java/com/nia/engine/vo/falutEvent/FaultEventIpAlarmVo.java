package com.nia.engine.vo.falutEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class FaultEventIpAlarmVo implements Serializable {
    private String strresname;
    private String stripaddr;
    private String strifname;
    private String strifdesc;
    private String strifspeed;
    private String striftype;
    private String strifoperstatus;
    private String nrenId;
    private String nrenName;
    private String nodeId;
    private String ifId;
    private int interridx;
    private int moduleSrl;
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
    private int stracknowncheckMemberSrl;

    @JsonProperty("nren_id")
    public String getNrenId() {
        return nrenId;
    }
    @JsonProperty("nren_name")
    public String getNrenName() {
        return nrenName;
    }
    @JsonProperty("node_id")
    public String getNodeId() {
        return nodeId;
    }
    @JsonProperty("if_id")
    public String getIfId() {
        return ifId;
    }
    @JsonProperty("module_srl")
    public int getModuleSrl() {
        return moduleSrl;
    }
    @JsonProperty("stracknowncheck_member_srl")
    public int getStracknowncheckMemberSrl() {
        return stracknowncheckMemberSrl;
    }
}
