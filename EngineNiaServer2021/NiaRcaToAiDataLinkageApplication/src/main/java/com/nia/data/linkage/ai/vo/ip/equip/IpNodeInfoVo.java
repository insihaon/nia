package com.nia.data.linkage.ai.vo.ip.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@Data
public class IpNodeInfoVo implements Serializable{
	private String node_num;
	private String node_id;
	private String node_nm;
	private String model_num;
	private String model_id;
	private String ip_addr;
	private String mac_addr;
	private String admin_yn;
	private String primary_gb;
	private String snmp_ver;
	private String snmp_community;
	private int snmp_port;
	private String latitude;
	private String longitude;
	private String netconf_port;
	private String netconf_id;
	private String netconf_pw;
	private String netconf_type;
	private String node_division;
	private String vendor;
	private String sflow_ip;
	private String up_datetime;
	private String chng_datetime;
	private String reg_datetime;
	private String data_source;

}