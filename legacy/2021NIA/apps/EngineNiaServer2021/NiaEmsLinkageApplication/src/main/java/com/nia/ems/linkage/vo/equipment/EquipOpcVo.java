package com.nia.ems.linkage.vo.equipment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class EquipOpcVo implements Serializable {
    private String sysname;
	private String mrsaSlot;
	private String unit;
	private String lambda;
	private String port;
	private String addSource;
	private String dropAddress;
}
