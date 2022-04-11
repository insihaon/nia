package com.nia.engine.vo.falutEvent;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class FaultEventSflowLogVo implements Serializable {
    private String strresip;
    private String strresname;
    private String strs_ip;
    private String strs_port;
    private String strd_ip;
    private String strd_port;
    private String strs_mac;
    private String strd_mac;
    private String strprotocol;
    private String stripv4tos;
    private String strchannel;
    private String strsenderip;
    private String strin_interface;
    private String strout_interface;
    private String strbytes_col;
    private String strcounts;
    private String dateregdate;
}
