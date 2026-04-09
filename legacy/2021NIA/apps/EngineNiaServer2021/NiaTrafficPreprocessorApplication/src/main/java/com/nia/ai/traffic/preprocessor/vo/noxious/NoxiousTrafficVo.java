package com.nia.ai.traffic.preprocessor.vo.noxious;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@ToString
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoxiousTrafficVo implements Serializable {
     private String ticket_id;
     private String ticket_type;
     private String strresip;
     private String strresname;
     private String strs_ip;
     private int strs_port;
     private String strd_ip;
     private int strd_port;
     private String strs_mac;
     private String strd_mac;
     private int strprotocol;
     private int stripv4tos;
     private String strchannel;
     private String strsenderip;
     private String strin_interface;
     private String strout_interface;
     private String strbytes_col;
     private int strcounts;
     private String dateregdate;
     private int anomaly;
}
