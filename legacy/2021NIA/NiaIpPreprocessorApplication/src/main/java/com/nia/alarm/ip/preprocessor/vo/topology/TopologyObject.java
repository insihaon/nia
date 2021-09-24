package com.nia.alarm.ip.preprocessor.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class TopologyObject implements Serializable{

	private String alarmno;
	private String linkId;
	private String trunkId;
	private String nwType; // NNI or UNI
	private String oppNescode;
	private String oppSysname;
	private String oppPtpName;
	private String oppPort;
	private String oppSlot;
}