package com.nia.data.linkage.ai.vo.trans.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class EquipInfoVo implements Serializable {

    private String tid;
    private String sysname;
    private String psu;
    private String model;
    private String sid;
    private String sw_ver;
    private String sw_chksum;
    private String sw_date;
    private String sw_time;
}
