package com.nia.data.linkage.ai.vo.trans.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class UniTopologyVo implements Serializable{

	private String linkId;
	private String sysnamea;
	private String ptpnamea;
	private String sysnamez;
	private String ptpnamez;
}
