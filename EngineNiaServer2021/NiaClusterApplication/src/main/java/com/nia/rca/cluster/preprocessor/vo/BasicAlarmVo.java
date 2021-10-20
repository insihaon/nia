
package com.nia.rca.cluster.preprocessor.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.rca.cluster.preprocessor.common.UtlDateHelper;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicAlarmVo implements Serializable {

	private String tmpClusterno;
	private String clusterno;
	private String clusternoOriginal;

    private String alarmno;
    private String sysname;
    private String equipCode;
    private String equipName;
    private String equiptype;
    private String unit;
    private Timestamp alarmtime;
    private Timestamp receivetime;
    private String alarmloc;
    private String alarmmsg;
    private String alarmmsgOriginal;
    private String alarmCode;
    private String alarmlevel;
    private String alarmType;
    private String domain;

    private String ifNum;
    private String ifId;
    private String ipAddr;

    private Timestamp cleartime;
    private String port;
    private String slot;
    private String ptpName;
	private TopologyObject topology;

    @JsonProperty("alarmtime")
    public void setAlarmtime(long alarmtime){
       try {
			this.alarmtime = UtlDateHelper.longToTimestamp(alarmtime);
		} catch (Exception e) {

		}
    }

    @JsonProperty("receivetime")
    public void setReceivetime(long receivetime) {
        try {
            this.receivetime = UtlDateHelper.longToTimestamp(receivetime);
        } catch (Exception e) {

        }
    }
}
