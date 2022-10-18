package com.nia.data.linkage.ai.vo.ipsdn.resource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class NodeVo implements Serializable {
	private int id;
	private String nodeName;
	private String loopBackAddr;
	private String mgmtAddr;


	@JsonProperty("Id")
	public int getId() {
		return id;
	}
	@JsonProperty("NodeName")
	public String getNodeName() {
		return nodeName;
	}

	@JsonProperty("LoopBackAddr")
	public String getLoopBackAddr() {
		return loopBackAddr;
	}

	@JsonProperty("MgmtAddr")
	public String getMgmtAddr() {
		return mgmtAddr;
	}
}