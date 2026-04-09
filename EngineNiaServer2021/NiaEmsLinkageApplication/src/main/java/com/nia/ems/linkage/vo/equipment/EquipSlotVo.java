package com.nia.ems.linkage.vo.equipment;

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
	private String pUnit;
	private String pPorts;
	private String act;
	private String state;
	private String degree;
	private String rxDegree;
	private String txDegree;
	private String adSide;
	private String currentUnit;
	private String currentPorts;
	private String pairSlot;
	private String netType;
}
