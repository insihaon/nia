package com.nia.data.linkage.ai.vo.ip.sflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class SflowLogVo implements Serializable {
    private String strresip;
    private String strresname;
    @JsonProperty("strs_ip")
    private String strsIp;
    @JsonProperty("strs_port")
    private String strsPort;
    @JsonProperty("strd_ip")
    private String strdIp;
    @JsonProperty("strd_port")
    private String strdPort;
    @JsonProperty("strs_mac")
    private String strsMac;
    @JsonProperty("strd_mac")
    private String strdMac;
//    private String strprotocol;
//    private String stripv4tos;
//    private String strchannel;
    private String strsenderip;
    private String strbytes_col;
    private String strcounts;
    private String dateregdate;
}
