package com.nia.engine.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class
ClusterInfoVo implements Serializable {
	private static final long serialVersionUID = 3170781290435579732L;

    private String tmpClusterNo;
    private String clusterNo;
    private String clusterType;
    private BasicAlarmVo basicAlarmVo;
}
