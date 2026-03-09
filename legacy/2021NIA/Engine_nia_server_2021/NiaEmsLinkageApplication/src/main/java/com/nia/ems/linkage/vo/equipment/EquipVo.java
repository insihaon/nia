package com.nia.ems.linkage.vo.equipment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class EquipVo implements Serializable {
    private String sysname;
    private String ip;
    private String equipType;
}
