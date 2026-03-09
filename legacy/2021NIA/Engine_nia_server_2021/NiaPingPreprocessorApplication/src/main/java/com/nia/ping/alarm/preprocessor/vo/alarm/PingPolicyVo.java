package com.nia.ping.alarm.preprocessor.vo.alarm;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
public class PingPolicyVo implements Serializable {
    private String policyId;
    private int wrFaultAvg;
    private int miFaultAvg;
    private int mjFaultAvg;
    private int crFaultAvg;
}
