package com.nia.data.linkage.ai.vo.trans.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class NNiTopologyVo implements Serializable{

	private String link_id;
	private String sysnamea;
	private String ptpnamea_bau;
	private String ptpnamea_pau;
	private String sysnamez;
	private String ptpnamez_bau;
	private String ptpnamez_pau;
	private String equip_type;
}
