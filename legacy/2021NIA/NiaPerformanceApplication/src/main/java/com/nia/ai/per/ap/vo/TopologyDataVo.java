package com.nia.ai.per.ap.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class TopologyDataVo implements Serializable{
	private String linkId;
	private String sysnamea;
	private String ptpnameaBau;
	private String ptpnameaPau;
	private String sysnamez;
	private String ptpnamezBau;
	private String ptpnamezPau;
}
