package com.nia.data.linkage.ai.vo.ipsdn.resource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class LinkVo implements Serializable {
	private int id;
	private int sifId;
	private int rifId;
	private int ospfMetric;


	@JsonProperty("Id")
	public int getId() {
		return id;
	}

	@JsonProperty("Sif_Id")
	public int getSifId() {
		return sifId;
	}

	@JsonProperty("Rif_Id")
	public int getRifId() {
		return rifId;
	}

	@JsonProperty("OspfMetric")
	public int getOspfMetric() {
		return ospfMetric;
	}

	@JsonProperty("Usaged")
	public int getUsaged() {
		return usaged;
	}

	private int usaged;
}