package com.nia.alarm.ip.preprocessor.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class TopologyNeVo implements Serializable{

	private String nodeNum;
	private String nodeId;
	private String ifNum;
	private String ifId;
	private String ipAddr;
	private String macAddr;

}