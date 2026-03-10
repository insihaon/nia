package com.nia.ems.linkage.vo.equipment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class EquipSipcVo implements Serializable {
    private String sysname;
    private String sysnameName;
}
