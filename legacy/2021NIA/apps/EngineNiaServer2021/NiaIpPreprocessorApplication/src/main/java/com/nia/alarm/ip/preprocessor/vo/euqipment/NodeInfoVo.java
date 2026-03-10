package com.nia.alarm.ip.preprocessor.vo.euqipment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@Data
public class NodeInfoVo implements Serializable{
	private String nodeNum;
	private String nodeId;
	private String nodeNm;
	private String modelNum;
	private String modelId;
	private String modelNm;
	private String ipAddr;
	private String macAddr;
	private String adminYn;
	private String primaryGb;
	private String snmpVer;
	private String snmpCommunity;
	private int snmpPort;
	private String latitude;
	private String longitude;
	private String netconfPort;
	private String netconfId;
	private String netconfPw;
	private String netconfType;
	private String nodeDivision;
	private String vendor;
	private String sflowIp;
	private Timestamp upDatetime;
	private Timestamp chngDatetime;
	private Timestamp regDatetime;
	private String dataSource;

}