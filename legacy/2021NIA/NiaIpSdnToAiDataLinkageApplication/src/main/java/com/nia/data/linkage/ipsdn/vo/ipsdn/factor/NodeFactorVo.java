package com.nia.data.linkage.ipsdn.vo.ipsdn.factor;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;
@Component
@Scope(value = "prototype")
@Data
public class NodeFactorVo implements Serializable {
	private int sdn_node_id;
	private int id;
	private String strresid;
	private String strresnm;
	private double cpuUsage;
	private double memUsage;
	private Timestamp measured_datetime;






}