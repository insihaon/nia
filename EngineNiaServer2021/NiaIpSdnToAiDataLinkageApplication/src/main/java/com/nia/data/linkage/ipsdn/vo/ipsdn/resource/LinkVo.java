package com.nia.data.linkage.ipsdn.vo.ipsdn.resource;
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
//	private int vsifId;
//	private String speed;
//	private int activeStandby;
//	private float latencyStd;
	private int usaged;


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

//	@JsonProperty("Vsif_Id")
//	public int getVsifId(){
//		return vsifId;
//	}
//
//	@JsonProperty
//	public String getSpeed(){
//		return speed;
//	}
//	@JsonProperty("ActiveStandby")
//	public int getActiveStandby(){
//		return activeStandby;
//	}
//	@JsonProperty("LatencyStd")
//	public float getLatencyStd(){
//		return latencyStd;
//	}



}