package com.nia.engine.vo.falutEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.engine.vo.TopologyObject;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Administrator
 *
 */
@Component
@Data
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaultEventAlarmDataVo implements Serializable {
	private String alarmno;
	private String sysname;
	private String equiptype;
    private String alarmtime;
    private String receivetime;
	private String alarmloc;
	private String alarmmsg;
	private String unit;
	private String alarmlevel;
}
