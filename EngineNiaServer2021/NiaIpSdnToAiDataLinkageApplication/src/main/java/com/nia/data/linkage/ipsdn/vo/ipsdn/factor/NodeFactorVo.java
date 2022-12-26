package com.nia.data.linkage.ipsdn.vo.ipsdn.factor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;
@Component
@Scope(value = "prototype")
@Data
public class NodeFactorVo implements Serializable {
	private int sdnNodeId;
	private int id;
	private String strresid;
	private String strresnm;
	private double cpuUsage;
	private double memUsage;
	private Timestamp measuredDatetime;
	@JsonProperty("sdn_node_id")
	public int getSdnNodeId() {
		return sdnNodeId;
	}

	@JsonProperty("measured_datetime")
	public Timestamp getMeasuredDatetime() {
		return measuredDatetime;
	}
}