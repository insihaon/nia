package com.nia.data.linkage.ai.vo.ipsdn.factor;
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
	private int id;
	private double cpuUsage;
	private double memUsage;
	private int nodeId;
	private Timestamp measuredDatetime;

	@JsonProperty("Id")
	public int getId() {
		return id;
	}

	@JsonProperty("CpuUsage")
	public double getCpuUsage() {
		return cpuUsage;
	}

	@JsonProperty("MemUsage")
	public double getMemUsage() {
		return memUsage;
	}

	@JsonProperty("Node_Id")
	public int getNodeId() {
		return nodeId;
	}

	@JsonProperty("MeasuredDatetime")
	public Timestamp getMeasuredDatetime() {
		return measuredDatetime;
	}
}