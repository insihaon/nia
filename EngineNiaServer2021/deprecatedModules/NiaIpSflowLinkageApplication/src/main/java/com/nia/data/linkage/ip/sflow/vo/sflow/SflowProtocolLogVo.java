package com.nia.data.linkage.ip.sflow.vo.sflow;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class SflowProtocolLogVo implements Serializable {
    private int intIndex;
    private String strResIP;
    private String strResName;
    private String strRank;
    private String strProtocol;
    private String strBytes;
    private String strUnit;
    private Timestamp dateRegDate;
}
