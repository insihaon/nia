package com.nia.data.linkage.ipsdn.vo.ipsdn.syslog;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
public class SyslogDataVo implements Serializable {

	private int collectSeq;
	private String collectHost;
	private String source;
	private String appname;
	private String hostname;
	private int facilityCode;
	private String facility;
	private int severityCode;
	private String severity;
	private String syslogMessage;
	private String syslogProcid;
	private Timestamp collectTimestamp;

	@JsonProperty("CollectSeq")
	public int getCollectSeq() {
		return collectSeq;
	}

	@JsonProperty("CollectHost")
	public String getCollectHost() {
		return collectHost;
	}

	@JsonProperty("Source")
	public String getSource() {
		return source;
	}

	@JsonProperty("Appname")
	public String getAppname() {
		return appname;
	}

	@JsonProperty("Hostname")
	public String getHostname() {
		return hostname;
	}

	@JsonProperty("FacilityCode")
	public int getFacilityCode() {
		return facilityCode;
	}

	@JsonProperty("Facility")
	public String getFacility() {
		return facility;
	}

	@JsonProperty("SeverityCode")
	public int getSeverityCode() {
		return severityCode;
	}

	@JsonProperty("Severity")
	public String getSeverity() {
		return severity;
	}

	@JsonProperty("SyslogMessage")
	public String getSyslogMessage() {
		return syslogMessage;
	}

	@JsonProperty("SyslogProcid")
	public String getSyslogProcid() {
		return syslogProcid;
	}

	@JsonProperty("CollectTimestamp")
	public Timestamp getCollectTimestamp() {
		return collectTimestamp;
	}

}