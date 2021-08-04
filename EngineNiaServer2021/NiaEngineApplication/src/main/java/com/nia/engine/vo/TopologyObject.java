package com.nia.engine.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class TopologyObject implements Serializable{
	private static final long serialVersionUID = 4709879230908109670L;

	private String alarmno;
	private String linkId;
	private String nwType; // NNI or UNI
	private String oppSysname;
	private String oppPtpName;
	private String oppSlot;
	private String oppPort;
	private String trunkId;
}
