package com.nia.alarm.preprocessor.vo.topology;

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
    private String trunkId;
    private String trunkName;
    private int routenum;
    private String sysname;
    private String upPtpnameBau;
    private String upPtpnamePau;
    private String downPtpnameBau;
    private String downPtpnamePau;

}
