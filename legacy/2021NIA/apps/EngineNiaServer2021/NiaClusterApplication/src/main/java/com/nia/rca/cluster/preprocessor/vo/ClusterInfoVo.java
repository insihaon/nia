package com.nia.rca.cluster.preprocessor.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
@Data
public class ClusterInfoVo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterInfoVo.class);

	private String tmpClusterNo;
	private String clusterNo;
	private String clusterType;
	private BasicAlarmVo basicAlarmVo;

}