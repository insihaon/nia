
package com.nia.rca.cluster.preprocessor.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.rca.cluster.preprocessor.common.UtlDateHelper;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@ToString
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicAlarmVo implements Serializable {

	private String tmpClusterno;
	private String clusterno;
	private String clusternoOriginal;

	@JsonProperty("alarmno")
	private String alarmno;
	@JsonProperty("sysname")
    private String sysname;
	@JsonProperty("equiptype")
    private String equiptype;
	@JsonProperty("unit")
    private String unit;
	@JsonProperty("alarmtime")
    private Timestamp alarmtime;
	@JsonProperty("receivetime")
	private Timestamp receivetime;
	@JsonProperty("alarmloc")
    private String alarmloc;
	@JsonProperty("alarmmsg")
    private String alarmmsg;
	@JsonProperty("alarmmsgOriginal")
    private String alarmmsgOriginal;
	@JsonProperty("alarmlevel")
    private String alarmlevel;
	@JsonProperty("description")
    private String description;
	@JsonProperty("cleartime")
	private Timestamp cleartime;
	@JsonProperty("ptpName")
	private String ptpName;
	@JsonProperty("port")
	private String port;
	@JsonProperty("slot")
	private String slot;
	@JsonProperty("topologyObject")
	private TopologyObject topology;

	public String getTmpClusterno() {
		return tmpClusterno;
	}

	public void setTmpClusterno(String tmpClusterno) {
		this.tmpClusterno = tmpClusterno;
	}

	public String getClusterno() {
		return clusterno;
	}

	public void setClusterno(String clusterno) {
		this.clusterno = clusterno;
	}

	@JsonProperty("alarmno")
	public String getAlarmno() {
		return alarmno;
	}

	@JsonProperty("alarmno")
	public void setAlarmno(String alarmno) {
		this.alarmno = alarmno;
	}

	@JsonProperty("sysname")
	public String getSysname() {
		return sysname;
	}

	@JsonProperty("sysname")
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	@JsonProperty("equiptype")
	public String getEquiptype() {
		return equiptype;
	}

	@JsonProperty("equiptype")
	public void setEquiptype(String equiptype) {
		this.equiptype = equiptype;
	}

	@JsonProperty("unit")
	public String getUnit() {
		return unit;
	}

	@JsonProperty("unit")
	public void setUnit(String unit) {
		this.unit = unit;
	}

	@JsonProperty("alarmtime")
	public Timestamp getAlarmtime() {
		return alarmtime;
	}

    @JsonProperty("alarmtime")
    public void setAlarmtime(long alarmtime){
       try {
			this.alarmtime = UtlDateHelper.longToTimestamp(alarmtime);
		} catch (Exception e) {

		}
    }

	@JsonProperty("receivetime")
	public Timestamp getReceivetime() {
		return receivetime;
	}

    @JsonProperty("receivetime")
    public void setReceivetime(long receivetime) {
        try {
            this.receivetime = UtlDateHelper.longToTimestamp(receivetime);
        } catch (Exception e) {

        }
    }

	@JsonProperty("alarmloc")
	public String getAlarmloc() {
		return alarmloc;
	}

	@JsonProperty("alarmloc")
	public void setAlarmloc(String alarmloc) {
		this.alarmloc = alarmloc;
	}

	@JsonProperty("alarmmsg")
	public String getAlarmmsg() {
		return alarmmsg;
	}

	@JsonProperty("alarmmsg")
	public void setAlarmmsg(String alarmmsg) {
		this.alarmmsg = alarmmsg;
	}

	@JsonProperty("alarmmsgOriginal")
	public String getAlarmmsgOriginal() {
		return alarmmsgOriginal;
	}

	@JsonProperty("alarmmsgOriginal")
	public void setAlarmmsgOriginal(String alarmmsgOriginal) {
		this.alarmmsgOriginal = alarmmsgOriginal;
	}

	@JsonProperty("alarmlevel")
	public String getAlarmlevel() {
		return alarmlevel;
	}

	@JsonProperty("alarmlevel")
	public void setAlarmlevel(String alarmlevel) {
		this.alarmlevel = alarmlevel;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("cleartime")
	public Timestamp getCleartime() {
		return cleartime;
	}

	@JsonProperty("cleartime")
	public void setCleartime(Timestamp cleartime) {
		this.cleartime = cleartime;
	}

	@JsonProperty("ptpName")
	public String getPtpName() {
		return ptpName;
	}

	@JsonProperty("ptpName")
	public void setPtpName(String ptpName) {
		this.ptpName = ptpName;
	}

	@JsonProperty("port")
	public String getPort() {
		return port;
	}

	@JsonProperty("port")
	public void setPort(String port) {
		this.port = port;
	}

	@JsonProperty("slot")
	public String getSlot() {
		return slot;
	}

	@JsonProperty("slot")
	public void setSlot(String slot) {
		this.slot = slot;
	}

	@JsonProperty("topology")
	public TopologyObject getTopology() {
		return topology;
	}

	@JsonProperty("topology")
	public void setTopology(TopologyObject topology) {
		this.topology = topology;
	}

}
