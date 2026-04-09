package com.nia.data.linkage.ipsdn.vo.ipsdn.resource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class InterfaceVo implements Serializable {
	private int id;
	private String ifName;
	private int nodeId;
	private String ipAddr;
	private int hwIfId;

	@JsonProperty("Id")
	public int getId() {
		return id;
	}

	@JsonProperty("IfName")
	public String getIfName() {
		return ifName;
	}

	@JsonProperty("NodeId")
	public int getNodeId() {
		return nodeId;
	}

	@JsonProperty("IpAddr")
	public String getIpAddr() {
		return ipAddr;
	}

	@JsonProperty("HwIf_Id")
	public int getHwIfId() {
		return hwIfId;
	}
}