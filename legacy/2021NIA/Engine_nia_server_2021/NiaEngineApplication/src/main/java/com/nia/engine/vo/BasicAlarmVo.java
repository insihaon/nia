package com.nia.engine.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nia.engine.common.UtlDateHelper;

/**
 * @author Administrator
 *
 */
@Component
@Data
@ToString
@Scope(value = "prototype")
public class BasicAlarmVo implements Serializable {
	private static final long serialVersionUID = -1638535903952501352L;

    @JsonProperty("alarmno")
	private String alarmno;
	@JsonProperty("clusterno")
	private String clusterno;
	@JsonProperty("sysname")
	private String sysname;
	@JsonProperty("equiptype")
	private String equiptype;
    @JsonProperty("alarmtime")
    private Timestamp alarmtime;
    @JsonProperty("receivetime")
    private Timestamp receivetime;
	@JsonProperty("alarmloc")
	private String alarmloc;
	@JsonProperty("alarmmsg")
	private String alarmmsg;
	@JsonProperty("unit")
	private String unit;
	@JsonProperty("alarmlevel")
	private String alarmlevel;
	@JsonProperty("topologyObject")
	private TopologyObject topology;
	@JsonProperty("ptpname")
	private String ptpname;
	@JsonProperty("slot")
	private String slot;

}
