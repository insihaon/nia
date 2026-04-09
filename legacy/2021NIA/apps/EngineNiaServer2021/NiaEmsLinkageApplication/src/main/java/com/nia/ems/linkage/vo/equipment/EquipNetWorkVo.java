package com.nia.ems.linkage.vo.equipment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class EquipNetWorkVo implements Serializable {
    private String sid;
    private String aidBau;
    private String aidPau;
    private String pSid;
    private String pAidBau;
    private String pAidPau;
}
