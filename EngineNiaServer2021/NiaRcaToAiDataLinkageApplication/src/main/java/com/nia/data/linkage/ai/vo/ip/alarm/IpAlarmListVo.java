package com.nia.data.linkage.ai.vo.ip.alarm;

import com.nia.data.linkage.ai.vo.ip.equip.IpBackboneLinkVo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component
@Scope(value = "prototype")
public class IpAlarmListVo implements Serializable {
    private ArrayList<IpAlarmVo> data;
}
