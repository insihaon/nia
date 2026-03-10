package com.nia.engine.vo.engineToAi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
import com.nia.engine.vo.sdn.factor.NodeFactorListVo;
import com.nia.engine.vo.sdn.traffic.SdnTrafficListVo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EngineToAiNoxVo {

    @JsonProperty("ticket_id")
    private int ticketId;

    @JsonProperty("ticket_type")
    private String ticketType;

    @JsonProperty("strresnum")
    private String strresnum;

    @JsonProperty("strresip")
    private String strresip;

    @JsonProperty("strresname")
    private String strresname;

    @JsonProperty("strs_ip")
    private String strsIp;

    @JsonProperty("strs_port")
    private String strsPort;

    @JsonProperty("strd_ip")
    private String strdIp;

    @JsonProperty("strd_port")
    private int strdPort;

    @JsonProperty("strs_mac")
    private String strsMac;

    @JsonProperty("strd_mac")
    private String strdMac;

    @JsonProperty("strprotocol")
    private int strprotocol;

    @JsonProperty("stripv4tos")
    private int stripv4tos;

    @JsonProperty("strchannel")
    private String strchannel;

    @JsonProperty("strsenderip")
    private String strsenderip;

    @JsonProperty("strin_interface")
    private String strinInterface;

    @JsonProperty("strout_interface")
    private String stroutInterface;

    @JsonProperty("strbytes_col")
    private int strbytesCol;

    @JsonProperty("strcounts")
    private int strcounts;

    @JsonProperty("dateregdate")
    private String dateregdate;

    @JsonProperty("ai_accuracy")
    private int aiAccuracy;

}
