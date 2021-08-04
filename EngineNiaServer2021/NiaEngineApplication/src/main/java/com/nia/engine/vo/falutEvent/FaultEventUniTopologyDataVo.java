package com.nia.engine.vo.falutEvent;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class FaultEventUniTopologyDataVo implements Serializable{
	private String linkId;
	private String sysnamea;
	private String ptpnamea;
	private String shelfa;
	private String sysnamez;
	private String ptpnamez;
	private String shelfz;
}
