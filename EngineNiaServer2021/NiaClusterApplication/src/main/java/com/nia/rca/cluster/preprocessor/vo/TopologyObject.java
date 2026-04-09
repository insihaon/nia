package com.nia.rca.cluster.preprocessor.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
	private String oppIfId;
	private String oppIfNum;
    private Timestamp insertTime;
}
