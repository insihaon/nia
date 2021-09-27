package com.nia.data.linkage.ip.sflow.vo.sflow;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class SflowLogVo implements Serializable {
    private int intIndex;
    private String strResIP;
    private String strResName;
    private String strRank;
    private String strSIP;
    private String strSPORT;
    private String strDIP;
    private String strDPORT;
    private String strSMAC;
    private String strDMAC;
    private String strProtocol;
    private String strIPv4ToS;
    private String strChannel;
    private String strIP;
    private String strPort;
    private String strMAC;
    private String strInterface;
    private String strSenderIP;
    private String strInInterface;
    private String strOutInterface;
    private String strBytes;
    private String strUnit;
    private Timestamp dateRegDate;
}
