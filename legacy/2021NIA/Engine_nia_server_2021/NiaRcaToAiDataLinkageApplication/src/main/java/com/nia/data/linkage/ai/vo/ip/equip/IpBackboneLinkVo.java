package com.nia.data.linkage.ai.vo.ip.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class IpBackboneLinkVo implements Serializable{
	private String src_node_num;
	private String src_node_id;
	private String src_if_num;
	private String src_if_id;
	private String dest_node_num;
	private String dest_node_id;
	private String dest_if_num;
	private String dest_if_id;
	private String link_desc;
	private String vlan;
	private String tag;
	private String src_ip_addr;
	private String src_mac_addr;
	private String dest_ip_addr;
	private String dest_mac_addr;
	private String prefix;
	private String topology;
	private String bandwidth;
	private String link_type;
	private String link_div;
	private String chng_datetime;
	private String reg_datetime;
	private String data_source;
	private String circuit_num;
}
