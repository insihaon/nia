package com.nia.syslog.preprocessor.vo.resource;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class IpNodeInfoVo implements Serializable{
	private String nodeNum;
	private String nodeId;
	private String nodeNm;
	private String modelNum;
	private String modelId;
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
	private String upDatetime;
	private String chngDatetime;
	private String regDatetime;
	private String dataSource;

}