package com.nia.rca.cluster.preprocessor.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopologyObject implements Serializable{

	@JsonProperty("alarmno")
	private String alarmno;
	@JsonProperty("linkId")
	private String linkId;
	@JsonProperty("trunkId")
	private String trunkId;
	@JsonProperty("nwType")
	private String nwType; // NNI or UNI
	@JsonProperty("oppSysname")
	private String oppSysname;
	@JsonProperty("oppPtpName")
	private String oppPtpName;
	@JsonProperty("oppPort")
	private String oppPort;
	@JsonProperty("oppSlot")
	private String oppSlot;
    private Timestamp insertTime;

    @JsonProperty("alarmno")
	public String getAlarmno() {
		return alarmno;
	}

	@JsonProperty("alarmno")
	public void setAlarmno(String alarmno) {
		this.alarmno = alarmno;
	}

	@JsonProperty("linkId")
	public String getLinkId() {
		return linkId;
	}

	@JsonProperty("linkId")
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	@JsonProperty("trunkId")
	public String getTrunkId() {
		return trunkId;
	}

	@JsonProperty("trunkId")
	public void setTrunkId(String trunkId) {
		this.trunkId = trunkId;
	}

	@JsonProperty("nwType")
	public String getNwType() {
		return nwType;
	}

	@JsonProperty("nwType")
	public void setNwType(String nwType) {
		this.nwType = nwType;
	}

	@JsonProperty("oppSysname")
	public String getOppSysname() {
		return oppSysname;
	}

	@JsonProperty("oppSysname")
	public void setOppSysname(String oppSysname) {
		this.oppSysname = oppSysname;
	}

	@JsonProperty("oppPtpName")
	public String getOppPtpName() {
		return oppPtpName;
	}

	@JsonProperty("oppPtpName")
	public void setOppPtpName(String oppPtpName) {
		this.oppPtpName = oppPtpName;
	}

	@JsonProperty("oppPort")
	public String getOppPort() {
		return oppPort;
	}

	@JsonProperty("oppPort")
	public void setOppPort(String oppPort) {
		this.oppPort = oppPort;
	}

	@JsonProperty("oppSlot")
	public String getOppSlot() {
		return oppSlot;
	}

	@JsonProperty("oppSlot")
	public void setOppSlot(String oppSlot) {
		this.oppSlot = oppSlot;
	}

	public Timestamp getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}
}
