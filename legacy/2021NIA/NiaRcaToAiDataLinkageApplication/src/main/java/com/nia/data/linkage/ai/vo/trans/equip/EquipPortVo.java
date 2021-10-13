package com.nia.data.linkage.ai.vo.trans.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class EquipPortVo implements Serializable {
    private String tid;
	private String port;
	private String portType;
}
