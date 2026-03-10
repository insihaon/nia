package com.nia.engine.vo.aiToEngine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiToEngineNoxVo {

    @JsonProperty("ticket_id")
    private String ticketId;

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
    private String strdPort;

    @JsonProperty("strs_mac")
    private String strsMac;

    @JsonProperty("strd_mac")
    private String strdMac;

    @JsonProperty("strprotocol")
    private String strprotocol;

    @JsonProperty("stripv4tos")
    private String stripv4tos;

    @JsonProperty("strchannel")
    private String strchannel;

    @JsonProperty("strsenderip")
    private String strsenderip;

    @JsonProperty("strin_interface")
    private String strinInterface;

    @JsonProperty("strout_interface")
    private String stroutInterface;

    @JsonProperty("strbytes_col")
    private String strbytesCol;

    @JsonProperty("strcounts")
    private String strcounts;

    @JsonProperty("dateregdate")
    private String dateregdate;

    @JsonProperty("ai_accuracy")
    private String aiAccuracy;

    @JsonProperty("zero1_model")
    private int zero1Model;

    @JsonProperty("zero1_entropy")
    private double zero1Entropy;

}
