package com.nia.data.linkage.ai.vo.trans.equip;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
@ToString
public class RoadmRepeaterRouteVo implements Serializable {
    private String trunk_id;
    private String trunk_name;
    private String routenum;
    private String sysname;
    private String up_ptpname_bau;
    private String up_ptpname_pau;
    private String down_ptpname_bau;
    private String down_ptpname_pau;
}
