package com.nia.data.linkage.ipsdn.vo.ipsdn.traffic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;
@Component
@Scope(value = "prototype")
@Data
public class LinkTrafficVo implements Serializable {
	private int sdnNodeId;
	private int id;
	private String strresid;
	private String strresnm;
	private int sdnIfId;
	private String strifid;
	private String strifnm;
	private int fltbpsin;
	private int fltbpsout;
	private int fltppsin;
	private int fltppsout;
	private String nrenId;
	private String nrenName;
	private Timestamp measuredDatetime;

	@JsonProperty("sdn_node_id")
	public int getSdnNodeId() {
		return sdnNodeId;
	}

	@JsonProperty("sdn_if_id")
	public int getSdnIfId() {
		return sdnIfId;
	}

	@JsonProperty("nren_id")
	public String getnrenId() {
		return nrenId;
	}

	@JsonProperty("nren_name")
	public String getNrenName() {
		return nrenName;
	}

	@JsonProperty("measured_datetime")
	public Timestamp getMeasuredDatetime() {
		return measuredDatetime;
	}
}