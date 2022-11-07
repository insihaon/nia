package com.nia.ip.sdn.syslog.linkage.vo.syslog;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nia.ip.sdn.syslog.linkage.common.UtlDateHelper;

@Component
@Data
@Scope(value = "prototype")
public class SyslogCollectVo implements Serializable {
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
	private String partitionKey;

	public void setSyslogCollectVo(SyslogDataVo syslogDataVo) {
		this.setCollectSeq(syslogDataVo.getCollectSeq());
		this.setCollectHost(syslogDataVo.getTags().getHost());
		this.setSource(syslogDataVo.getTags().getSource());
		this.setAppname(syslogDataVo.getTags().getAppname());
		this.setHostname(syslogDataVo.getTags().getHostname());
		this.setFacility(syslogDataVo.getTags().getFacility());
		this.setSeverity(syslogDataVo.getTags().getSeverity());
		this.setCollectTimestamp(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(syslogDataVo.getTimestamp() * 1000L))));
		this.setFacilityCode(syslogDataVo.getFields().getFacilityCode());
		this.setSeverityCode(syslogDataVo.getFields().getSeverityCode());
		this.setSyslogMessage(syslogDataVo.getFields().getMessage());
		this.setSyslogProcid(syslogDataVo.getFields().getProcid());
		this.setPartitionKey(String.valueOf(syslogDataVo.getCollectSeq()));
	}
}