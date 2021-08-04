package com.nia.alarm.preprocessor.vo.alarm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.alarm.preprocessor.common.UtlDateHelper;
import com.nia.alarm.preprocessor.vo.topology.TmpTopologyVo;
import com.nia.alarm.preprocessor.vo.topology.TopologyObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicAlarmVo implements Serializable {

	@JsonProperty("alarmno")
	private String alarmno;
	@JsonProperty("sysname")
    private String sysname;
	private String equipName;
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

	private Timestamp cleartime;
	private String port;
	private String slot;
	private String ptpName;
	private TopologyObject topologyObject;
	private TmpTopologyVo tmpTopologyVo;

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

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
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

	public String getAlarmmsgOriginal() {
		return alarmmsgOriginal;
	}

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

	public Timestamp getCleartime() {
		return cleartime;
	}

	public void setCleartime(Timestamp cleartime) {
		this.cleartime = cleartime;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getPtpName() {
		return ptpName;
	}

	public void setPtpName(String ptpName) {
		this.ptpName = ptpName;
	}

	public TopologyObject getTopologyObject() {
		return topologyObject;
	}

	public void setTopologyObject(TopologyObject topologyObject) {
		this.topologyObject = topologyObject;
	}

	public TmpTopologyVo getTmpTopologyVo() {
		return tmpTopologyVo;
	}

	public void setTmpTopologyVo(TmpTopologyVo tmpTopologyVo) {
		this.tmpTopologyVo = tmpTopologyVo;
	}
}
