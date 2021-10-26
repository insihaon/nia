package com.nia.data.linkage.ai.vo.trans.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class EquipSlotVo implements Serializable {
	private String sysname;
	private String slot;
	private String p_unit;
	private String p_ports;
	private String act;
	private String state;
	private String degree;
	private String rx_degree;
	private String tx_degree;
	private String ad_side;
	private String current_unit;
	private String current_ports;
	private String pair_slot;
	private String net_type;
}
